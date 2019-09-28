-- Version of player model.
-- default_character_v1:
--	minetest_game before 25 nov 2016
--	3d_armor before 27 nov 2016	(overrides model from minetest_game)
-- default_character_v2:
--	minetest_game after 25 nov 2016
--	3d_armor after 27 nov 2016	(overrides model from minetest_game)

local valid_player_model_versions =  {
	default_character_v1 = true,
	default_character_v2 = true,
}

local player_model_version = minetest.setting_get("player_model_version")
if not player_model_version or player_model_version == "" then
	player_model_version = "default_character_v1"
elseif not  valid_player_model_versions[player_model_version] then
	error("Invalid value for player_model_version in minetest.conf: " .. player_model_version)
end

-- Localize to avoid table lookups
local vector_new = vector.new
local math_pi = math.pi
local math_sin = math.sin
local math_abs = math.abs
local table_remove = table.remove
local get_animation = default.player_get_animation

-- Animation alias
local STAND = 1
local WALK = 2
local MINE = 3
local WALK_MINE = 4
local SIT = 5
local LAY = 6

-- Bone alias
local BODY = "Body"
local HEAD = "Head"
local CAPE = "Cape"
local LARM = "Arm_Left"
local RARM = "Arm_Right"
local LLEG = "Leg_Left"
local RLEG = "Leg_Right"
local TORSO = "Torso"

local bone_positions = {
	default_character_v1 = {
		[BODY] 			= 		vector_new(0, -3.5, 0),
		[HEAD] 			= 		vector_new(0, 6.75, 0),
		[CAPE] 			= 		vector_new(0, 6.75, 1.1),
		[LARM] 			= 		vector_new(2, 6.75, 0),
		[RARM] 			= 		vector_new(-2, 6.75, 0),
		[LLEG] 			= 		vector_new(-1, 0, 0),
		[RLEG]		 	= 		vector_new(1, 0, 0),
		[TORSO]         = 		vector_new(0, 0, 0)
	},
	default_character_v2 = {
		[BODY] = vector_new(0, -3.5, 0),
		[HEAD] = vector_new(0, 6.75, 0),
		[CAPE] = vector_new(0, 6.75, 1.2),
		[LARM] = vector_new(3, 5.75, 0),
		[RARM] = vector_new(-3, 5.75, 0),
		[LLEG] = vector_new(1, 0, 0),
		[RLEG] = vector_new(-1, 0, 0),
		[TORSO]= vector_new(0, 0, 0)
	}
}

local bone_rotations = {
	default_character_v1 = {
		[BODY] 			= 		vector_new(0, 0, 0),
		[HEAD] 			= 		vector_new(0, 0, 0),
		[CAPE] 			= 		vector_new(180, 0, 0),
		[LARM]		 	= 		vector_new(180, 0, 0),
		[RARM] 			= 		vector_new(180, 0, 0),
		[LLEG] 			= 		vector_new(0, 0, 0),
		[RLEG]			= 		vector_new(0, 0, 0),
		[TORSO]         = 		vector_new(0, 0, 0)
	},
	default_character_v2 = {
		[BODY] = vector_new(0, 0, 0),
		[HEAD] = vector_new(0, 0, 0),
		[CAPE] = vector_new(0, 0, 0),
		[LARM] = vector_new(180, 0, 0),
		[RARM] = vector_new(180, 0, 0),
		[LLEG] = vector_new(0, 0, 0),
		[RLEG] = vector_new(0, 0, 0),
		[TORSO]= vector_new(0, 0, 0)
	}
}

local bone_rotation = bone_rotations[player_model_version]
local bone_position = bone_positions[player_model_version]
if not bone_rotation or not bone_position then
	error("Internal error: invalid player_model_version: " .. player_model_version)
end

local bone_rotation_cache = {}

local function rotate(player, bone, x, y, z)
	local default_rotation = bone_rotation[bone]
	local rotation = {
		x = (x or 0) + default_rotation.x,
		y = (y or 0) + default_rotation.y,
		z = (z or 0) + default_rotation.z
	}

	local player_cache = bone_rotation_cache[player]
	local rotation_cache = player_cache[bone]

	if not rotation_cache
	or rotation.x ~= rotation_cache.x
	or rotation.y ~= rotation_cache.y
	or rotation.z ~= rotation_cache.z then
		player_cache[bone] = rotation
		player:set_bone_position(bone, bone_position[bone], rotation)
	end
end

local step = 0
local previous_look_yaw = {}
local previous_yaw = {}
local previous_left_arm = {}
local previous_left_shoulder = {}
local previous_left_arm_OrthoX = {}
local previous_left_arm_OrthoZ = {}
local previous_look_pitch = {}
local previous_right_arm = {}
local previous_right_shoulder = {}
local previous_right_arm_OrthoX = {}
local previous_right_arm_OrthoZ = {}
local previous_left_leg = {}
local previous_head = {}
local previous_left_leg_OrthoX = {}
local previous_left_leg_OrthoZ = {}
local previous_right_leg = {}
local previous_right_leg_OrthoX = {}
local previous_right_leg_OrthoZ = {}
local previous_torso_rot = {}
local previous_torso_roll = {}
local previous_torso_x = {}
local previous_left_shoulder_flag = {}
local previous_right_shoulder_flag = {}
local left_arm = {}
local left_shoulder = {}
local left_arm_OrthoX = {}
local left_arm_OrthoZ = {}
local right_arm = {}
local right_shoulder = {}
local right_arm_OrthoX = {}
local right_arm_OrthoZ = {}
local left_leg = {}
local left_leg_OrthoX = {}
local left_leg_OrthoZ = {}
local right_leg= {}
local head= {}
local right_leg_OrthoX = {}
local right_leg_OrthoZ = {}
local torso_rot = {}
local torso_roll = {}
local torso_x = {}
local left_shoulder_flag = {}
local right_shoulder_flag = {}
local face = {}
local pdata_face = {}
local gender = {}
local look_pitch = {}
local look_kpitch = {}
local look_kyaw = {}
local look_kroll = {}
local animation_speed = {}
local arm = {}

local animations = {
	
	[STAND] = function(player)
		rotate(player, TORSO)
		rotate(player, CAPE)
		rotate(player, LARM)
		rotate(player, RARM)
		rotate(player, LLEG)
		rotate(player, RLEG)
	end,

	[WALK] = function(player)
		local swing = math_sin(step * 4 * animation_speed[player])
		rotate(player, CAPE, swing * -30 - 35)
		rotate(player, LARM, swing * -40)
		--core.log("[WALK] = function for LARM   swing:     " .. swing * -40)
		rotate(player, RARM, swing * 40)
		rotate(player, LLEG, swing * 40)
		rotate(player, RLEG, swing * -40)
	end,

	[MINE] = function(player)
		local pitch = look_pitch[player]
		local speed = animation_speed[player]
		local swing = math_sin(step * 4 * speed)
		local hand_swing = math_sin(step * 8 * speed)
		rotate(player, CAPE, swing * -5 - 10)
		rotate(player, LARM)
		rotate(player, RARM, hand_swing * 20 + 80, hand_swing * 5 - 3, 10)
		rotate(player, LLEG)
		rotate(player, RLEG)
	end,

	[WALK_MINE] = function(player)
		local pitch = look_pitch[player]
		local speed = animation_speed[player]

		local swing = math_sin(step * 4 * speed)
		local hand_swing = math_sin(step * 8 * speed)

		rotate(player, CAPE, swing * -30 - 35)
		rotate(player, LARM, swing * -40)
		rotate(player, RARM, hand_swing * 20 + 80, hand_swing * 5 - 3, 10)
		rotate(player, LLEG, swing * 40)
		rotate(player, RLEG, swing * -40)
	end,

	[SIT] = function(player)
		local body_position = vector_new(bone_position[BODY])
		body_position.y = body_position.y - 6

		player:set_bone_position(BODY, body_position, {x = 0, y = 0, z = 0})

		rotate(player, LARM)
		rotate(player, RARM)
		rotate(player, LLEG, 90)
		rotate(player, RLEG, 90)
	end,

	[LAY] = function(player)
		rotate(player, HEAD)
		rotate(player, CAPE)
		rotate(player, LARM)
		rotate(player, RARM)
		rotate(player, LLEG)
		rotate(player, RLEG)

		local body_position = {x = 0, y = -9, z = 0}
		local body_rotation = {x = 270, y = 0, z = 0}

		player:set_bone_position(BODY, body_position, body_rotation)
	end
}

local function update_kinect_look_pitch(player)
	
	local kpitch = player:get_kinect_pitch()      
	if look_kpitch[player] ~= kpitch then
		look_kpitch[player] = kpitch
	end
	
	--core.log("LUA MOD::    player: "   .. player:get_player_name() .. " look_kpitch    :   " .. look_kpitch[player])
	
end

local function update_kinect_look_yaw(player)
	
	local kyaw = -player:get_kinect_yaw()  
	if look_kyaw[player] ~= kyaw then
		look_kyaw[player] = kyaw
	end
	
	--core.log("LUA MOD::    player: "   .. player:get_player_name() .. " look_kyaw    :   " .. look_kyaw[player])
	
end

local function update_kinect_look_roll(player)
	
	local kroll = -player:get_kinect_roll()       
	if look_kroll[player] ~= kroll then
		look_kroll[player] = kroll
	end
	
	--core.log("LUA MOD::    player: "   .. player:get_player_name() .. " look_kroll    :   " .. look_kroll[player])
	
end

local function update_left_arm(player)
	
	local kinectleftarm = player:get_kinect_arm_l()	
	if left_arm[player] ~= kinectleftarm then
		left_arm[player] = kinectleftarm
	end
	
	--core.log("LUA MOD::    player: "   .. player:get_player_name() .. " left_arm    :   " .. left_arm[player])
	
end

local function update_left_shoulder(player)
	
	local kinectleftshoulder = player:get_kinect_shoulder_l()	    
	if left_shoulder[player] ~= kinectleftshoulder then
		left_shoulder[player] = kinectleftshoulder
	end
	
	--core.log("LUA MOD::    player: "   .. player:get_player_name() .. " left_shoulder    :   " .. left_shoulder[player])
	
end

local function update_left_arm_OrthoX(player)
	
	local kinectleftarmOrthoX = player:get_kinect_arm_l_OrthoX()	   
	if left_arm_OrthoX[player] ~= kinectleftarmOrthoX then
		left_arm_OrthoX[player] = kinectleftarmOrthoX
	end
	
	--core.log("LUA MOD::    player: "   .. player:get_player_name() .. " left_arm_OrthoX    :   " .. left_arm_OrthoX[player])
	
end

local function update_left_arm_OrthoZ(player)
	
	local kinectleftarmOrthoZ = player:get_kinect_arm_l_OrthoZ()	    
	if left_arm_OrthoZ[player] ~= kinectleftarmOrthoZ then
		left_arm_OrthoZ[player] = kinectleftarmOrthoZ
	end
	
	--core.log("LUA MOD::    player: "   .. player:get_player_name() .. " left_arm_OrthoZ    :   " .. left_arm_OrthoZ[player])
	
end


local function update_right_arm(player)
	
	local kinectrightarm = player:get_kinect_arm_r()	   	
	if right_arm[player] ~= kinectrightarm then
		right_arm[player] = kinectrightarm
	end
	
	--core.log("LUA MOD::    player: "   .. player:get_player_name() .. " right_arm    :   " .. right_arm[player])
	
end

local function update_right_shoulder(player)
	
	local kinectrightshoulder = player:get_kinect_shoulder_r()	    
	if right_shoulder[player] ~= kinectrightshoulder then
		right_shoulder[player] = kinectrightshoulder
	end
	
	--core.log("LUA MOD::    player: "   .. player:get_player_name() .. " right_shoulder    :   " .. right_shoulder[player])
	
end

local function update_right_arm_OrthoX(player)
	
	local kinectrightarmOrthoX = player:get_kinect_arm_r_OrthoX()	    
	if right_arm_OrthoX[player] ~= kinectrightarmOrthoX then
		right_arm_OrthoX[player] = kinectrightarmOrthoX
	end
	
	--core.log("LUA MOD::    player: "   .. player:get_player_name() .. " right_arm_OrthoX    :   " .. right_arm_OrthoX[player])
	
end

local function update_right_arm_OrthoZ(player)
	
	local kinectrightarmOrthoZ = player:get_kinect_arm_r_OrthoZ()	    
	if right_arm_OrthoZ[player] ~= kinectrightarmOrthoZ then
		right_arm_OrthoZ[player] = kinectrightarmOrthoZ
	end

	--core.log("LUA MOD::    player: "   .. player:get_player_name() .. " right_arm_OrthoZ    :   " .. right_arm_OrthoZ[player])
	
end

local function update_left_leg(player)
	
	local kinectleftleg = player:get_kinect_leg_l()	    
	if left_leg[player] ~= kinectleftleg then
		left_leg[player] = kinectleftleg
	end
	
	--core.log("LUA MOD::    player: "   .. player:get_player_name() .. " left_leg   :   " .. left_leg[player])
	
end

local function update_left_leg_OrthoX(player)
	
	local kinectleftlegOrthoX = player:get_kinect_leg_l_OrthoX()	   
	if left_leg_OrthoX[player] ~= kinectleftlegOrthoX then
		left_leg_OrthoX[player] = kinectleftlegOrthoX
	end
	
	--core.log("LUA MOD::    player: "   .. player:get_player_name() .. " left_leg_OrthoX    :   " .. left_leg_OrthoX[player])
	
end

local function update_left_leg_OrthoZ(player)
	
	local kinectleftlegOrthoZ = player:get_kinect_leg_l_OrthoZ()	    
	if left_leg_OrthoZ[player] ~= kinectleftlegOrthoZ then
		left_leg_OrthoZ[player] = kinectleftlegOrthoZ
	end
	
	--core.log("LUA MOD::    player: "   .. player:get_player_name() .. " left_leg_OrthoZ    :   " .. left_leg_OrthoZ[player])
	
end

local function update_right_leg(player)
	
	local kinectrightleg = player:get_kinect_leg_r()	   	
	if right_leg[player] ~= kinectrightleg then
		right_leg[player] = kinectrightleg
	end
	
	--core.log("LUA MOD::    player: "   .. player:get_player_name() .. " right_leg   :   " .. right_leg[player])
	
end

local function update_right_leg_OrthoX(player)
	
	local kinectrightlegOrthoX = player:get_kinect_leg_r_OrthoX()	    
	if right_leg_OrthoX[player] ~= kinectrightlegOrthoX then
		right_leg_OrthoX[player] = kinectrightlegOrthoX
	end
	
	--core.log("LUA MOD::    player: "   .. player:get_player_name() .. " right_leg_OrthoX    :   " .. right_leg_OrthoX[player])
	
end

local function update_right_leg_OrthoZ(player)
	
	local kinectrightlegOrthoZ = player:get_kinect_leg_r_OrthoZ()	    
	if right_leg_OrthoZ[player] ~= kinectrightlegOrthoZ then
		right_leg_OrthoZ[player] = kinectrightlegOrthoZ
	end
	
	--core.log("LUA MOD::    player: "   .. player:get_player_name() .. " right_leg_OrthoZ    :   " .. right_leg_OrthoZ[player])
	
end

local function update_torso_rot(player)
	
	local kinecttorsorot = -player:get_kinect_torso_rot()	   
	if torso_rot[player] ~= kinecttorsorot then
		torso_rot[player] = kinecttorsorot
	end
	
	--core.log("LUA MOD::    player: "   .. player:get_player_name() .. " update_torso_rot    :   " .. torso_rot[player])
	
end

local function update_torso_roll(player)
	
	local kinecttorsoroll = -player:get_kinect_torso_roll()	   
	if torso_roll[player] ~= kinecttorsoroll then
		torso_roll[player] = kinecttorsoroll
	end
	
	--core.log("LUA MOD::    player: "   .. player:get_player_name() .. " update_torso_roll    :   " .. torso_roll[player])
	
end


local function update_torso_x(player)
	
	local kinecttorsox = player:get_kinect_torso_x()	    
	if torso_x[player] ~= kinecttorsox then
		torso_x[player] = kinecttorsox
	end
	
	--core.log("LUA MOD::    player: "   .. player:get_player_name() .. " update_torso_x   :   " .. torso_x[player])
	
end

local function update_left_shoulder_flag(player)
	
	local kinectleftshoulderflag = player:get_kinect_left_shoulder_flag()	    
	if left_shoulder_flag[player] ~= kinectleftshoulderflag then
		left_shoulder_flag[player] = kinectleftshoulderflag
	end
	
	--core.log("LUA MOD::    player: "   .. player:get_player_name() .. " update_left_shoulder_flag   :   " .. left_shoulder_flag[player])
	
end

local function update_right_shoulder_flag(player)
	
	local kinectrightshoulderflag = player:get_kinect_right_shoulder_flag()	    
	if right_shoulder_flag[player] ~= kinectrightshoulderflag then
		right_shoulder_flag[player] = kinectrightshoulderflag
	end
	
	--core.log("LUA MOD::    player: "   .. player:get_player_name() .. " update_right_shoulder_flag   :   " .. right_shoulder_flag[player])
	
end

local function update_pdata_face(player)
	
	local pdataface = player:get_pdata_face()	   
	--local pdataface = 1
	if pdata_face[player] ~= pdataface then
		pdata_face[player] = pdataface
	end
	
	--core.log("LUA MOD::    player: "   .. player:get_player_name() .. " update_pdata_face    :   " .. pdataface)
	
end

local function update_gender(player)
	
	local mygender = player:get_gender()	   
	--local pdataface = 1
	if gender[player] ~= mygender then
		gender[player] = mygender
	end
	
	--core.log("LUA MOD::    player: "   .. player:get_player_name() .. " update_gender    :   " .. mygender)
	
end

local function update_kinect_face(player)
	
	local kinectface = player:get_kinect_face()	   
	--local kinectface = 1
	if face[player] ~= kinectface then
		face[player] = kinectface
	end
	
	--core.log("LUA MOD::    player: "   .. player:get_player_name() .. " update_kinect_face    :   " .. face[player])
	
end

local function kinect_pitch_yaw_roll_moving(player)
	--rotate(player, HEAD, look_kpitch[player] , -look_kyaw[player])
	
	--player:set_bone_position(HEAD, {x = 0, y = 6.75, z = 0}, {x = look_kpitch[player]/1.5, y = -look_kyaw[player]/1.5, z = -look_kroll[player]/1.5})	
	
	player:set_bone_position_quaternion(HEAD, {x = 0, y = 6.75, z = 0}, look_kpitch[player], -look_kyaw[player],  -look_kroll[player])	
		
end

local function update_left_arm_moving(player)
		--rotate(player, LARM, left_arm_OrthoX[player],left_arm_OrthoZ[player])
		    	
	   --core::quaternion quat_begin(LARM * core::DEGTORAD); 
	

		if left_arm[player] == 0 and left_arm_OrthoX[player] == 0 and left_arm_OrthoZ[player] == 0 then
			player:set_bone_position(LARM, {x = 2.3, y = 6.4 + left_shoulder[player], z = 0}, {x=0,y=0,z=180})
		else
			if left_shoulder_flag[player] == 1 then
				player:set_bone_position_quaternion(LARM, {x = 2.3, y = 6.4 + left_shoulder[player]*5, z = 0}, 180-left_arm[player], left_arm_OrthoX[player],  left_arm_OrthoZ[player])		
			else
				player:set_bone_position_quaternion(LARM, {x = 2.3, y = 6.4, z = 0}, 180-left_arm[player], left_arm_OrthoX[player],  left_arm_OrthoZ[player])	
			end
		end
		
		
		--player:set_bone_position(LARM, {x = 2.3, y = 6.4 + left_shoulder[player]/1.5, z = 0}, {x=0,y=0,z=180})	
		
		--core.log(" left_arm[player]   :   " .. left_arm[player].. " left_arm_OrthoX[player]   :   " .. left_arm_OrthoX[player].." left_arm_OrthoZ[player]   :   " .. left_arm_OrthoZ[player])
		
		--if left_shoulder[player] > 0 and right_shoulder[player] > 0 then
		--	player:set_bone_position(BODY, {x = 0, y = -3.5 - left_shoulder[player] - right_shoulder[player], z = 0}, {})		
		--else
		--	player:set_bone_position(BODY, {x = 0, y = -3.5 , z = 0}, {})		
		--end
end

local function update_right_arm_moving(player)
		--rotate(player, RARM, right_arm[player],right_arm_OrthoX[player],right_arm_OrthoZ[player])
		--player:set_bone_position(RARM, {x = -2, y = 6.4  + right_shoulder[player], z = 0}, {x = 360+180-right_arm_OrthoX[player], y = 0, z = -right_arm[player]/1.5})	
		if right_arm[player] == 0 and right_arm_OrthoX[player] == 0 and right_arm_OrthoZ[player] == 0 then
			player:set_bone_position(RARM, {x = -2.3, y = 6.4 + right_shoulder[player], z = 0}, {x=0,y=0,z=180})
		else
			if right_shoulder_flag[player] == 1 then
				player:set_bone_position_quaternion(RARM, {x = -2.3, y = 6.4 + right_shoulder[player]*5, z = 0}, 180-right_arm[player], right_arm_OrthoX[player],  right_arm_OrthoZ[player])	
			else
				player:set_bone_position_quaternion(RARM, {x = -2.3, y = 6.4, z = 0}, 180-right_arm[player], right_arm_OrthoX[player],  right_arm_OrthoZ[player])		
			end
		end
			
		--if left_shoulder[player] > 0 and right_shoulder[player] > 0 then
		--	player:set_bone_position(BODY, {x = 0, y = -3.5 - 2*left_shoulder[player] - 2*right_shoulder[player], z = 0}, {})		
		--end
			
end


local function update_left_leg_moving(player)
		--player:set_bone_position(LLEG, {x = -0.75, y = 0, z = 0}, {x = 360+left_leg_OrthoX[player]/2, y = 0, z = -left_leg[player]})	
		
		player:set_bone_position_quaternion(LLEG, {x = -1, y = 0, z = 0}, 360+left_leg[player], left_leg_OrthoX[player],  left_leg_OrthoZ[player])	
		
end
			
local function update_right_leg_moving(player)
		--player:set_bone_position(RLEG, {x = 0.75, y = 0, z = 0}, {x = 360+right_leg_OrthoX[player]/2, y = 0, z = right_leg[player]})	
		
		player:set_bone_position_quaternion(RLEG, {x = 1, y = 0, z = 0}, 360+right_leg[player], right_leg_OrthoX[player],  right_leg_OrthoZ[player])	
end

local function update_torso_moving(player)

			player:set_bone_position(TORSO, {x = 0, y = 0, z = 0}, {x = 0, y = torso_rot[player]*0.88, z = 0})	
			--player:set_bone_position(BODY, {x = 0, y = -3.5 , z = 0}, {x = 0, y = 0, z = -torso_roll[player]*1.5})	
	
end

local function update_face_moving(player)
		
	local facevalue = face[player]
	local pdatafacevalue = pdata_face[player]
	local mygender = gender[player]
	
	if	pdatafacevalue == 2 then
		player:set_properties({
			textures = {mygender .. "_MinetestModel25072017_0008_008.png", },   
			
		})
	else 
		if	facevalue == 0 then
		player:set_properties({
			textures = {mygender .. "_MinetestModel25072017_0000_000.png", },
			
		})
		else
			if  facevalue == 0 or facevalue == 1 or facevalue == 2 or facevalue == 3 or facevalue == 4 or facevalue == 5 or facevalue == 6 or facevalue == 7 or facevalue == 8 or facevalue == 9 then
			player:set_properties({
				textures = {mygender .. "_MinetestModel25072017_000" .. facevalue .. "_00" .. facevalue .. ".png", },   
			
			})
			else
			player:set_properties({
				textures = {mygender .. "_MinetestModel25072017_00" .. facevalue .. "_00" .. facevalue .. ".png", },   
			
			})
			end
		end
	end
	
	
		
end

local function set_animation_speed(player, sneak)
	local speed = sneak and 0.75 or 2

	if animation_speed[player] ~= speed then
		animation_speed[player] = speed
	end
end

local previous_animation = {}

local function set_animation(player, anim)
	if (anim == WALK or anim == MINE or anim == WALK_MINE) 
	or (previous_animation[player] ~= anim) then
		previous_animation[player] = anim
		animations[anim](player)
	end
end

local players = {}
local player_list = {}
local player_count = 0

local function update_players()
	players = {}

	local position = 0

	for player, joined in pairs(player_list) do
		if joined and player:is_player_connected() then
			position = position + 1
			players[position] = player
		end
	end

	player_count = position
end

minetest.register_on_joinplayer(function(player)
	
	local playername = player:get_player_name()
	
	if playername == "ICAT_KINECT" then
		player:setpos({x=75, y=2.5, z=-261})
		player:set_look_yaw(6)
	end
	
	if playername == "ICAT_RIGHT" then
		player:setpos({x=71.7, y=2.5, z=-259.9})
		player:set_look_yaw(4.5)
	end
	
	if playername == "ICAT_KINECT_NEW" then
		player:setpos({x=75, y=2.5, z=-259})
		player:set_look_yaw(3)
	end
	
	bone_rotation_cache[player] = {}
	previous_yaw[player] = {}
	previous_look_yaw[player] = {}
	player_list[player] = true
	update_players()
end)

minetest.register_on_leaveplayer(function(player)
	bone_rotation_cache[player] = nil

	look_pitch[player] = nil
	look_kpitch[player] = nil
	look_kyaw[player] = nil
	look_kroll[player] = nil
	animation_speed[player] = nil
	previous_left_arm[player] = nil
	previous_left_arm_OrthoX[player] = nil
	previous_left_arm_OrthoZ[player] = nil
	previous_look_pitch[player] = nil
	previous_right_arm[player] = nil
	previous_right_arm_OrthoX[player] = nil
	previous_right_arm_OrthoZ[player] = nil
	previous_left_leg[player] = nil
	previous_left_leg_OrthoX[player] = nil
	previous_left_leg_OrthoZ[player] = nil
	previous_right_leg[player] = nil
	previous_right_leg_OrthoX[player] = nil
	previous_right_leg_OrthoZ[player] = nil
	left_arm[player] = nil
	left_shoulder[player] = nil
	left_arm_OrthoX[player] = nil
	left_arm_OrthoZ[player] = nil
	right_arm[player] = nil
	right_shoulder[player] = nil
	left_leg[player] = nil
	previous_head[player] = nil
	head[player] = nil
	right_leg[player] = nil
	previous_look_yaw[player] = nil
	previous_yaw[player] = nil
	previous_animation[player] = nil
	player_list[player] = nil
	update_players()
end)

minetest.register_globalstep(function(dtime)
	if player_count == 0 then return end

	step = step + dtime
	if step >= 3600 then
		step = 1
	end

	for i = 1, player_count do
		local player = players[i]
			local animation = get_animation(player).animation
			
			if animation == "lay" then
				set_animation(player, LAY)

				if #previous_yaw[player] ~= 0 then
					previous_yaw[player] = {}
				end
			else 
				local controls = player:get_player_control()
				local sneak = controls.sneak

					
					
					local kpitch = player:get_kinect_pitch()  

						
						
						update_gender(player)
						--update_kinect_look_pitch(player)
						--update_kinect_look_yaw(player)
						--update_kinect_look_roll(player)
						--kinect_pitch_yaw_roll_moving(player)
						--update_left_arm(player)
						--update_left_shoulder(player)
						--update_left_arm_OrthoX(player)
						--update_left_arm_OrthoZ(player)
						--update_left_arm_moving(player)
						--update_right_arm(player)
						--update_right_shoulder(player)
						--update_right_arm_OrthoX(player)
						--update_right_arm_OrthoZ(player)
						--update_right_arm_moving(player)
						--update_left_leg(player)
						--update_left_leg_OrthoX(player)
						--update_left_leg_OrthoZ(player)
						--update_left_leg_moving(player)
						--update_right_leg(player)
						--update_right_leg_OrthoX(player)
						--update_right_leg_OrthoZ(player)
						--update_right_leg_moving(player)
						--update_torso_rot(player)
						--update_torso_roll(player)
						--update_torso_x(player)
						--update_left_shoulder_flag(player)
						--update_right_shoulder_flag(player)
						update_pdata_face(player)
						update_kinect_face(player)
						--update_torso_moving(player)
						update_face_moving(player)
					
			
					
				if animation == "walk" then
					set_animation_speed(player, sneak)
					set_animation(player, WALK)
				elseif animation == "mine" then
					set_animation_speed(player, sneak)
					set_animation(player, MINE)
				elseif animation == "walk_mine" then
					set_animation_speed(player, sneak)
					set_animation(player, WALK_MINE)
				elseif animation == "sit" then
					set_animation(player, SIT)
				else
					set_animation(player, STAND)
				end
			end
		
		
	end
end)
