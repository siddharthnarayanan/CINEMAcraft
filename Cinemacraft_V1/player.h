/*
Minetest
Copyright (C) 2010-2013 celeron55, Perttu Ahola <celeron55@gmail.com>

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation; either version 2.1 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along
with this program; if not, write to the Free Software Foundation, Inc.,
51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*/

#ifndef PLAYER_HEADER
#define PLAYER_HEADER

#include "irrlichttypes_bloated.h"
#include "inventory.h"
#include "constants.h" // BS
#include "threading/mutex.h"
#include <list>

#define PLAYERNAME_SIZE 20

#define PLAYERNAME_ALLOWED_CHARS "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_"
#define PLAYERNAME_ALLOWED_CHARS_USER_EXPL "'a' to 'z', 'A' to 'Z', '0' to '9', '-', '_'"

struct PlayerControl
{
	PlayerControl()
	{
		up = false;
		down = false;
		left = false;
		right = false;
		jump = false;
		aux1 = false;
		sneak = false;
		LMB = false;
		RMB = false;
		pitch = 0;
		yaw = 0;
	}
	PlayerControl(
		bool a_up,
		bool a_down,
		bool a_left,
		bool a_right,
		bool a_jump,
		bool a_aux1,
		bool a_sneak,
		bool a_LMB,
		bool a_RMB,
		float a_pitch,
		float a_yaw
	)
	{
		up = a_up;
		down = a_down;
		left = a_left;
		right = a_right;
		jump = a_jump;
		aux1 = a_aux1;
		sneak = a_sneak;
		LMB = a_LMB;
		RMB = a_RMB;
		pitch = a_pitch;
		yaw = a_yaw;
	}
	bool up;
	bool down;
	bool left;
	bool right;
	bool jump;
	bool aux1;
	bool sneak;
	bool LMB;
	bool RMB;
	float pitch;
	float yaw;
};

class Map;
class IGameDef;
struct CollisionInfo;
class PlayerSAO;
struct HudElement;
class Environment;

// IMPORTANT:
// Do *not* perform an assignment or copy operation on a Player or
// RemotePlayer object!  This will copy the lock held for HUD synchronization
class Player
{
public:

	Player(IGameDef *gamedef, const char *name);
	virtual ~Player() = 0;

	virtual void move(f32 dtime, Environment *env, f32 pos_max_d)
	{}
	virtual void move(f32 dtime, Environment *env, f32 pos_max_d,
			std::vector<CollisionInfo> *collision_info)
	{}

	v3f getSpeed()
	{
		return m_speed;
	}

	void setSpeed(v3f speed)
	{
		m_speed = speed;
	}

	v3f getPosition()
	{
		return m_position;
	}

	v3f getPositionForKinect()
	{
		return m_position_for_kinect;
	}

	v3f getKinectPosition()
	{
		return m_kinect_position;
	}

	v3s16 getLightPosition() const;

	v3f getEyeOffset()
	{
		float eye_height = camera_barely_in_ceiling ? 1.5f : 1.625f;
		return v3f(0, BS * eye_height, 0);
	}

	v3f getEyePosition()
	{
		return m_position + getEyeOffset();
	}

	void setKinectData(bool value)
	{
		kinectData = value;
	}

	void setKinectPosition(const v3f &kinectposition)
	{
		if (kinectposition != m_kinect_position)
			m_dirty = true;
		m_kinect_position = kinectposition;
	}

	virtual void setPosition(const v3f &position)
	{
		if (position != m_position)
			m_dirty = true;
		m_position = position;


		//dstream << "       m_position.X     : " << m_position.X << std::endl;
		//dstream << "       m_position.Y     : " << m_position.Y << std::endl;
		//dstream << "       m_position.Z     : " << m_position.Z << std::endl;
	}

	virtual void setPositionForKinect(const v3f &position)
	{
		if (position != m_position_for_kinect)
			m_dirty = true;
		m_position_for_kinect = position;


		//dstream << "       m_position.X     : " << m_position.X << std::endl;
		//dstream << "       m_position.Y     : " << m_position.Y << std::endl;
		//dstream << "       m_position.Z     : " << m_position.Z << std::endl;
	}

	void setPitch(f32 pitch)  //KINECT TO DO ??
	{
		
		if (pitch != m_pitch)
			m_dirty = true;

		m_pitch = pitch;
	}

	virtual void setYaw(f32 yaw)
	{
		if (yaw != m_yaw)
			m_dirty = true;
		m_yaw = yaw;
	}

	f32 getPitch()
	{
		return m_pitch;
	}

	f32 getYaw()
	{
		return m_yaw;
	}

	f32 getKinectRoll()
	{
		//22222dstream << "player:  " << m_name << "        getKinectRoll() :  " << m_kinect_roll << std::endl;
		if (m_kinect_roll<180000000 && m_kinect_roll>-180000000)
			return m_kinect_roll;
		else
			return 0;
	}
	
	f32 getKinectYaw()
	{
		//22222dstream << "player:  " << m_name << "        getKinectYaw() :  " << m_kinect_yaw << std::endl;
		if (m_kinect_yaw<180000000 && m_kinect_yaw>-180000000)
			return m_kinect_yaw;
		else
			return 0;
	}
	
	f32 getKinectPitch()
	{
		//22222dstream << "player:  " <<m_name << "        getKinectPitch() :  " << m_kinect_pitch << std::endl;
		if (m_kinect_pitch<180000000 && m_kinect_pitch>-180000000)
		return m_kinect_pitch;
		else
			return 0;
	}

	f32 getKinectLeftArm()
	{
		
		//22222dstream << "player:  " << m_name << "        getKinectLeftArm() :  " << m_kinect_left_arm << std::endl;
		if (m_kinect_left_arm<180000000 && m_kinect_left_arm>-180000000)
			return m_kinect_left_arm;
		else
			return 0;
	}
	
	f32 getKinectLeftShoulder()
	{
		
		//22222dstream << "player:  " << m_name << "        getKinectLeftShoulder() :  " << m_kinect_left_shoulder << std::endl;
		if (m_kinect_left_shoulder<180000000 && m_kinect_left_shoulder>-180000000)
			return m_kinect_left_shoulder;
		else
			return 0;
	}

	f32 getKinectLeftArmOrthoX()
	{

		//222222dstream << "player:  " << m_name << "        getKinectLeftArmOrthoX() :  " << m_kinect_left_arm_OrthoX << std::endl;
		if (m_kinect_left_arm_OrthoX<180000000 && m_kinect_left_arm_OrthoX>-180000000)
			return m_kinect_left_arm_OrthoX;
		else
			return 0;
	}

	f32 getKinectLeftArmOrthoZ()
	{

		//22222dstream << "player:  " << m_name << "        getKinectLeftArmOrthoZ() :  " << m_kinect_left_arm_OrthoZ << std::endl;
		if (m_kinect_left_arm_OrthoZ<180000000 && m_kinect_left_arm_OrthoZ>-180000000)
			return m_kinect_left_arm_OrthoZ;
		else
			return 0;
	}

	f32 getKinectRightArm()
	{
		
		//22222dstream << "player:  " << m_name << "        getKinectRightArm() :  " << m_kinect_right_arm << std::endl;
		if (m_kinect_right_arm<180000000 && m_kinect_right_arm>-180000000)
			return m_kinect_right_arm;
		else
			return 0;
	}
	
	f32 getKinectRightShoulder()
	{
		
		//22222dstream << "player:  " << m_name << "        getKinectRightShoulder() :  " << m_kinect_right_shoulder << std::endl;
		if (m_kinect_right_shoulder<180000000 && m_kinect_right_shoulder>-180000000)
			return m_kinect_right_shoulder;
		else
			return 0;
	}

	f32 getKinectRightArmOrthoX()
	{

		//22222dstream << "player:  " << m_name << "        getKinectRightArmOrthoX() :  " << m_kinect_right_arm_OrthoX << std::endl;
		if (m_kinect_right_arm_OrthoX<180000000 && m_kinect_right_arm_OrthoX>-180000000)
			return m_kinect_right_arm_OrthoX;
		else
			return 0;
	}

	f32 getKinectRightArmOrthoZ()
	{

		//22222dstream << "player:  " << m_name << "        getKinectRightArmOrthoZ() :  " << m_kinect_right_arm_OrthoZ << std::endl;
		if (m_kinect_right_arm_OrthoZ<180000000 && m_kinect_right_arm_OrthoZ>-180000000)
			return m_kinect_right_arm_OrthoZ;
		else
			return 0;
	}

	f32 getKinectLeftLeg()
	{
		
		//22222dstream << "player:  " << m_name << "        getKinectLeftLeg() :  " << m_kinect_left_leg << std::endl;
		if (m_kinect_left_leg<180000000 && m_kinect_left_leg>-180000000)
			return m_kinect_left_leg;
		else
			return 0;
	}

	f32 getKinectLeftLegOrthoX()
	{

		//22222dstream << "player:  " << m_name << "        getKinectLeftLegOrthoX() :  " << m_kinect_left_leg_OrthoX << std::endl;
		if (m_kinect_left_leg_OrthoX<180000000 && m_kinect_left_leg_OrthoX>-180000000)
			return m_kinect_left_leg_OrthoX;
		else
			return 0;
	}

	f32 getKinectLeftLegOrthoZ()
	{

		//22222dstream << "player:  " << m_name << "        getKinectLeftLegOrthoZ() :  " << m_kinect_left_leg_OrthoZ << std::endl;
		if (m_kinect_left_leg_OrthoZ<180000000 && m_kinect_left_leg_OrthoZ>-180000000)
			return m_kinect_left_leg_OrthoZ;
		else
			return 0;
	}

	f32 getKinectRightLeg()
	{
		
		//22222dstream << "player:  " << m_name << "        getKinectRightLeg() :  " << m_kinect_right_leg << std::endl;
		if (m_kinect_right_leg<180000000 && m_kinect_right_leg>-180000000)
			return m_kinect_right_leg;
		else
			return 0;
	}

	f32 getKinectRightLegOrthoX()
	{

		//22222dstream << "player:  " << m_name << "        getKinectRightLegOrthoX() :  " << m_kinect_right_leg_OrthoX << std::endl;
		if (m_kinect_right_leg_OrthoX<180000000 && m_kinect_right_leg_OrthoX>-180000000)
			return m_kinect_right_leg_OrthoX;
		else
			return 0;
	}

	f32 getKinectRightLegOrthoZ()
	{

		//22222dstream << "player:  " << m_name << "        getKinectRightLegOrthoZ() :  " << m_kinect_right_leg_OrthoZ << std::endl;
		if (m_kinect_right_leg_OrthoZ<180000000 && m_kinect_right_leg_OrthoZ>-180000000)
			return m_kinect_right_leg_OrthoZ;
		else
			return 0;
	}

	f32 getKinectTorsoRot()
	{

		//22222dstream << "player:  " << m_name << "        getKinectRightLegOrthoZ() :  " << m_kinect_right_leg_OrthoZ << std::endl;
		if (m_kinect_torso_rot<180000000 && m_kinect_torso_rot>-180000000)
			return m_kinect_torso_rot;
		else
			return 0;
	}

	f32 getKinectTorsoRoll()
	{

		//22222dstream << "player:  " << m_name << "        getKinectTorsoRoll() :  " << m_kinect_torso_roll << std::endl;
		if (m_kinect_torso_roll<180000000 && m_kinect_torso_roll>-180000000)
			return m_kinect_torso_roll;
		else
			return 0;
	}
	
	
	f32 getKinectTorsoX()
	{

		//22222dstream << "player:  " << m_name << "        getKinectTorsoX() :  " << m_kinect_torso_x << std::endl;
		if (m_kinect_torso_x<180000000 && m_kinect_torso_x>-180000000)
			return m_kinect_torso_x;
		else
			return 0;
	}

	f32 getKinectLeftShoulderFlag()
	{

		//22222dstream << "player:  " << m_name << "        getKinectLeftShoulderFlag() :  " << m_kinect_left_shoulder_flag << std::endl;
		if (m_kinect_left_shoulder_flag<180000000 && m_kinect_left_shoulder_flag>-180000000)
			return m_kinect_left_shoulder_flag;
		else
			return 0;
	}

	f32 getKinectRightShoulderFlag()
	{

		//22222dstream << "player:  " << m_name << "        getKinectRightShoulderFlag() :  " << m_kinect_right_shoulder_flag << std::endl;
		if (m_kinect_right_shoulder_flag<180000000 && m_kinect_right_shoulder_flag>-180000000)
			return m_kinect_right_shoulder_flag;
		else
			return 0;
	}

	int getGender()
	{

		//22222dstream << "player:  " << m_name << "        getGender() :  " << m_gender_flag << std::endl;
		if (m_gender_flag<180000000 && m_gender_flag>-180000000)
			return m_gender_flag;
		else
			return 0;
	}
	
	int getKinectFace()
	{

		//22222dstream << "player:  " << m_name << "        getKinectFace() :  " << m_kinect_face << std::endl;
		if (m_kinect_face<180000000 && m_kinect_face>-180000000)
			return m_kinect_face;
		else
			return 0;
	}

	int getPureDataFace()
	{

		//22222dstream << "player:  " << m_name << "        getPureDataFace() :  " << m_puredata_face << std::endl;
		if (m_puredata_face<180000000 && m_puredata_face>-180000000)
			return m_puredata_face;
		else
			return 0;
	}
	
	void setKinectLeftArm(f32 kinectleftarm)  //KINECT TO DO ??
	{

		//22222dstream << "player:  " << m_name << " now running setKinectLeftArm () : Setting m_kinect_left_arm to " << m_kinect_left_arm << std::endl;
		if (kinectleftarm != m_kinect_left_arm)
			m_dirty = true;

		m_kinect_left_arm = kinectleftarm;
	}
	
	void setKinectLeftShoulder(f32 kinectleftshoulder)  //KINECT TO DO ??
	{

		//22222dstream << "player:  " << m_name << " now running setKinectRightArm () : Setting m_kinect_right_arm to " << m_kinect_right_arm << std::endl;
		if (kinectleftshoulder != m_kinect_left_shoulder)
			m_dirty = true;

		m_kinect_left_shoulder = kinectleftshoulder;
	}

	void setKinectLeftArmOrthoX(f32 kinectleftarmOrthoX)  //KINECT TO DO ??
	{

		//22222dstream << "player:  " << m_name << " now running setKinectLeftArmOrthoX () : Setting m_kinect_left_arm_OrthoX to " << m_kinect_left_arm_OrthoX << std::endl;
		if (kinectleftarmOrthoX != m_kinect_left_arm_OrthoX)
			m_dirty = true;

		m_kinect_left_arm_OrthoX = kinectleftarmOrthoX;
	}

	void setKinectLeftArmOrthoZ(f32 kinectleftarmOrthoZ)  //KINECT TO DO ??
	{

		//22222dstream << "player:  " << m_name << " now running setKinectLeftArmOrthoZ () : Setting m_kinect_left_arm_OrthoZ to " << m_kinect_left_arm_OrthoZ << std::endl;
		if (kinectleftarmOrthoZ != m_kinect_left_arm_OrthoZ)
			m_dirty = true;

		m_kinect_left_arm_OrthoZ = kinectleftarmOrthoZ;
	}

	void setKinectRightArm(f32 kinectrightarm)  //KINECT TO DO ??
	{

		//22222dstream << "player:  " << m_name << " now running setKinectRightArm () : Setting m_kinect_right_arm to " << m_kinect_right_arm << std::endl;
		if (kinectrightarm != m_kinect_right_arm)
			m_dirty = true;

		m_kinect_right_arm = kinectrightarm;
	}
	
	void setKinectRightShoulder(f32 kinectrightshoulder)  //KINECT TO DO ??
	{

		//22222dstream << "player:  " << m_name << " now running setKinectRightArm () : Setting m_kinect_right_arm to " << m_kinect_right_arm << std::endl;
		if (kinectrightshoulder != m_kinect_right_shoulder)
			m_dirty = true;

		m_kinect_right_shoulder = kinectrightshoulder;
	}

	void setKinectFace(int kinectface)  //KINECT TO DO ??
	{

		//22222dstream << "player:  " << m_name << " now running setKinectRightArm () : Setting m_kinect_right_arm to " << m_kinect_right_arm << std::endl;
		if (kinectface != m_kinect_face)
			m_dirty = true;

		m_kinect_face = kinectface;
	}

	void setPureDataFace(int puredataface)  //KINECT TO DO ??
	{

		//22222dstream << "player:  " << m_name << " now running setKinectRightArm () : Setting m_kinect_right_arm to " << m_kinect_right_arm << std::endl;
		if (puredataface != m_puredata_face)
			m_dirty = true;

		m_puredata_face = puredataface;
	}

	void setKinectRightArmOrthoX(f32 kinectrightarmOrthoX)  //KINECT TO DO ??
	{

		//22222dstream << "player:  " << m_name << " now running setKinectLeftArmOrthoX () : Setting m_kinect_left_arm_OrthoX to " << m_kinect_left_arm_OrthoX << std::endl;
		if (kinectrightarmOrthoX != m_kinect_right_arm_OrthoX)
			m_dirty = true;

		m_kinect_right_arm_OrthoX = kinectrightarmOrthoX;
	}

	void setKinectRightArmOrthoZ(f32 kinectrightarmOrthoZ)  //KINECT TO DO ??
	{

		//22222dstream << "player:  " << m_name << " now running setKinectLeftArmOrthoZ () : Setting m_kinect_left_arm_OrthoZ to " << m_kinect_left_arm_OrthoZ << std::endl;
		if (kinectrightarmOrthoZ != m_kinect_right_arm_OrthoZ)
			m_dirty = true;

		m_kinect_right_arm_OrthoZ = kinectrightarmOrthoZ;
	}

	void setKinectLeftLegOrthoX(f32 kinectleftlegOrthoX)  //KINECT TO DO ??
	{

		//22222dstream << "player:  " << m_name << " now running setKinectLeftLegOrthoX () : Setting m_kinect_left_leg_OrthoX to " << m_kinect_left_leg_OrthoX << std::endl;
		if (kinectleftlegOrthoX != m_kinect_left_leg_OrthoX)
			m_dirty = true;

		m_kinect_left_leg_OrthoX = kinectleftlegOrthoX;
	}

	void setKinectLeftLegOrthoZ(f32 kinectleftlegOrthoZ)  //KINECT TO DO ??
	{

		//22222dstream << "player:  " << m_name << " now running setKinectLeftLegOrthoZ () : Setting m_kinect_left_leg_OrthoZ to " << m_kinect_left_leg_OrthoZ << std::endl;
		if (kinectleftlegOrthoZ != m_kinect_left_leg_OrthoZ)
			m_dirty = true;

		m_kinect_left_leg_OrthoZ = kinectleftlegOrthoZ;
	}

	void setKinectLeftLeg(f32 kinectleftleg)  //KINECT TO DO ??
	{

		//22222dstream << "player:  " << m_name << " now running setKinectLeftLeg () : Setting m_kinect_left_leg to " << m_kinect_left_leg << std::endl;
		if (kinectleftleg != m_kinect_left_leg)
			m_dirty = true;

		m_kinect_left_leg = kinectleftleg;
	}

	void setKinectRightLegOrthoX(f32 kinectrightlegOrthoX)  //KINECT TO DO ??
	{

		//22222dstream << "player:  " << m_name << " now running setKinectRightLegOrthoX () : Setting m_kinect_right_leg_OrthoX to " << m_kinect_right_leg_OrthoX << std::endl;
		if (kinectrightlegOrthoX != m_kinect_right_leg_OrthoX)
			m_dirty = true;

		m_kinect_right_leg_OrthoX = kinectrightlegOrthoX;
	}

	void setKinectRightLegOrthoZ(f32 kinectrightlegOrthoZ)  //KINECT TO DO ??
	{

		//22222dstream << "player:  " << m_name << " now running setKinectRightLegOrthoZ () : Setting m_kinect_right_leg_OrthoZ to " << m_kinect_right_leg_OrthoZ << std::endl;
		if (kinectrightlegOrthoZ != m_kinect_right_leg_OrthoZ)
			m_dirty = true;

		m_kinect_right_leg_OrthoZ = kinectrightlegOrthoZ;
	}

	void setKinectRightLeg(f32 kinectrightleg)  //KINECT TO DO ??
	{

		//22222dstream << "player:  " << m_name << " now running setKinectRightLeg () : Setting m_kinect_right_leg to " << m_kinect_right_leg << std::endl;
		if (kinectrightleg != m_kinect_right_leg)
			m_dirty = true;

		m_kinect_right_leg = kinectrightleg;
	}

	void setKinectTorsoRot(f32 kinecttorsorot)  //KINECT TO DO ??
	{

		//22222dstream << "player:  " << m_name << " now running setKinectRightLeg () : Setting m_kinect_right_leg to " << m_kinect_right_leg << std::endl;
		if (kinecttorsorot != m_kinect_torso_rot)
			m_dirty = true;

		m_kinect_torso_rot = kinecttorsorot;
	}

	void setKinectTorsoRoll(f32 kinecttorsoroll)  //KINECT TO DO ??
	{

		//22222dstream << "player:  " << m_name << " now running setKinectTorsoRoll () : Setting m_kinect_torso_roll to " << m_kinect_right_leg << std::endl;
		if (kinecttorsoroll != m_kinect_torso_roll)
			m_dirty = true;

		m_kinect_torso_roll = kinecttorsoroll;
	}
	
	void setKinectTorsoX(f32 kinecttorsox)  //KINECT TO DO ??
	{

		//22222dstream << "player:  " << m_name << " now running setKinectLeftArm () : Setting m_kinect_left_arm to " << m_kinect_left_arm << std::endl;
		if (kinecttorsox != m_kinect_torso_x)
			m_dirty = true;

		m_kinect_torso_x = kinecttorsox;
	}

	void setKinectLeftShoulderFlag(f32 kinectleftshoulderflag)  //KINECT TO DO ??
	{

		//22222dstream << "player:  " << m_name << " now running setKinectLeftArm () : Setting m_kinect_left_arm to " << m_kinect_left_arm << std::endl;
		if (kinectleftshoulderflag != m_kinect_left_shoulder_flag)
			m_dirty = true;

		m_kinect_left_shoulder_flag = kinectleftshoulderflag;
	}

	void setKinectRightShoulderFlag(f32 kinectrightshoulderflag)  //KINECT TO DO ??
	{

		//22222dstream << "player:  " << m_name << " now running setKinectLeftArm () : Setting m_kinect_left_arm to " << m_kinect_left_arm << std::endl;
		if (kinectrightshoulderflag != m_kinect_right_shoulder_flag)
			m_dirty = true;

		m_kinect_right_shoulder_flag = kinectrightshoulderflag;
	}

	void setGender(int genderflag)  //KINECT TO DO ??
	{

		//22222dstream << "player:  " << m_name << " now running setKinectLeftArm () : Setting m_kinect_left_arm to " << m_kinect_left_arm << std::endl;
		if (genderflag != m_gender_flag)
			m_dirty = true;

		m_gender_flag = genderflag;
	}

	u16 getBreath()
	{
		return m_breath;
	}

	virtual void setBreath(u16 breath)
	{
		if (breath != m_breath)
			m_dirty = true;
		m_breath = breath;
	}

	f32 getRadPitch()
	{
		return -1.0 * m_pitch * core::DEGTORAD;
	}

	f32 getRadYaw()
	{
		return (m_yaw + 90.) * core::DEGTORAD;
	}

	void setKinectRoll(f32 kinectroll)  //KINECT TO DO ??
	{

		//22222dstream << "player:  " << m_name << " now running setRadKinectYaw () : Setting m_kinect_yaw to " << m_kinect_yaw << std::endl;
		if (kinectroll != m_kinect_roll)
			m_dirty = true;

		m_kinect_roll = kinectroll;
	}
	
	void setKinectYaw(f32 kinectyaw)  //KINECT TO DO ??
	{

		//22222dstream << "player:  " << m_name << " now running setRadKinectYaw () : Setting m_kinect_yaw to " << m_kinect_yaw << std::endl;
		if (kinectyaw != m_kinect_yaw)
			m_dirty = true;

		m_kinect_yaw = kinectyaw;
	}

	void setKinectPitch(f32 kinectpitch)  //KINECT TO DO ??
	{

		if (kinectpitch != m_kinect_pitch)
			m_dirty = true;
		m_kinect_pitch = kinectpitch;
	}

	const char *getName() const
	{
		return m_name;
	}

	aabb3f getCollisionbox()
	{
		return m_collisionbox;
	}

	u32 getFreeHudID() {
		size_t size = hud.size();
		for (size_t i = 0; i != size; i++) {
			if (!hud[i])
				return i;
		}
		return size;
	}

	void setHotbarItemcount(s32 hotbar_itemcount)
	{
		hud_hotbar_itemcount = hotbar_itemcount;
	}

	s32 getHotbarItemcount()
	{
		return hud_hotbar_itemcount;
	}

	void setHotbarImage(const std::string &name)
	{
		hud_hotbar_image = name;
	}

	std::string getHotbarImage()
	{
		return hud_hotbar_image;
	}

	void setHotbarSelectedImage(const std::string &name)
	{
		hud_hotbar_selected_image = name;
	}

	std::string getHotbarSelectedImage() {
		return hud_hotbar_selected_image;
	}

	void setSky(const video::SColor &bgcolor, const std::string &type,
		const std::vector<std::string> &params)
	{
		m_sky_bgcolor = bgcolor;
		m_sky_type = type;
		m_sky_params = params;
	}

	void getSky(video::SColor *bgcolor, std::string *type,
		std::vector<std::string> *params)
	{
		*bgcolor = m_sky_bgcolor;
		*type = m_sky_type;
		*params = m_sky_params;
	}

	void overrideDayNightRatio(bool do_override, float ratio)
	{
		m_day_night_ratio_do_override = do_override;
		m_day_night_ratio = ratio;
	}

	void getDayNightRatio(bool *do_override, float *ratio)
	{
		*do_override = m_day_night_ratio_do_override;
		*ratio = m_day_night_ratio;
	}

	void setLocalAnimations(v2s32 frames[4], float frame_speed)
	{
		for (int i = 0; i < 4; i++)
			local_animations[i] = frames[i];
		local_animation_speed = frame_speed;
	}

	void getLocalAnimations(v2s32 *frames, float *frame_speed)
	{
		for (int i = 0; i < 4; i++)
			frames[i] = local_animations[i];
		*frame_speed = local_animation_speed;
	}

	virtual bool isLocal() const
	{
		return false;
	}

	virtual PlayerSAO *getPlayerSAO()
	{
		return NULL;
	}

	virtual void setPlayerSAO(PlayerSAO *sao)
	{
		FATAL_ERROR("FIXME");
	}

	/*
		serialize() writes a bunch of text that can contain
		any characters except a '\0', and such an ending that
		deSerialize stops reading exactly at the right point.
	*/
	void serialize(std::ostream &os);
	void deSerialize(std::istream &is, std::string playername);

	bool checkModified() const
	{
		return m_dirty || inventory.checkModified();
	}

	void setModified(const bool x)
	{
		m_dirty = x;
		if (x == false)
			inventory.setModified(x);
	}

	// Use a function, if isDead can be defined by other conditions
	bool isDead() { return hp == 0; }

	bool got_teleported;
	bool touching_ground;
	// This oscillates so that the player jumps a bit above the surface
	bool in_liquid;
	// This is more stable and defines the maximum speed of the player
	bool in_liquid_stable;
	// Gets the viscosity of water to calculate friction
	u8 liquid_viscosity;
	bool is_climbing;
	bool swimming_vertical;
	bool camera_barely_in_ceiling;
	v3f eye_offset_first;
	v3f eye_offset_third;

	Inventory inventory;

	f32 movement_acceleration_default;
	f32 movement_acceleration_air;
	f32 movement_acceleration_fast;
	f32 movement_speed_walk;
	f32 movement_speed_crouch;
	f32 movement_speed_fast;
	f32 movement_speed_climb;
	f32 movement_speed_jump;
	f32 movement_liquid_fluidity;
	f32 movement_liquid_fluidity_smooth;
	f32 movement_liquid_sink;
	f32 movement_gravity;

	f32 last_pitch;
	f32 last_yaw;
	f32 last_kinect_pitch;
	f32 last_kinect_yaw;
	f32 last_kinect_roll;
	f32 last_kinect_left_arm;
	f32 last_kinect_left_arm_OrthoX;
	f32 last_kinect_left_arm_OrthoZ;
	f32	last_kinect_right_arm;
	f32 last_kinect_right_arm_OrthoX;
	f32 last_kinect_right_arm_OrthoZ;
	f32 last_kinect_left_leg;
	f32 last_kinect_left_leg_OrthoX;
	f32 last_kinect_left_leg_OrthoZ;
	f32	last_kinect_right_leg;
	f32 last_kinect_right_leg_OrthoX;
	f32 last_kinect_right_leg_OrthoZ;
	f32 last_kinect_torso_X;
	f32 last_kinect_torso_Y;
	f32 last_kinect_torso_Z;
	f32 last_kinect_torso_rot;
	f32 last_kinect_torso_roll;
	f32 last_kinect_left_shoulder;
	f32	last_kinect_right_shoulder;
	f32	last_kinect_face;
	f32 last_puredata_mouth;

	float physics_override_speed;
	float physics_override_jump;
	float physics_override_gravity;
	bool physics_override_sneak;
	bool physics_override_sneak_glitch;

	v2s32 local_animations[4];
	float local_animation_speed;

	v3f m_position;
	v3f m_kinect_position;
	v3f m_position_for_kinect;

	f32 kinecttorsoX = 0;
	f32 kinecttorsoY = 0;
	f32 kinecttorsoZ = 0;

	u16 hp;

	float hurt_tilt_timer;
	float hurt_tilt_strength;

	u16 protocol_version;
	u16 peer_id;

	std::string inventory_formspec;

	PlayerControl control;
	PlayerControl getPlayerControl()
	{
		return control;
	}

	u32 keyPressed;
	bool kinectData = false;
	bool mirror = false;
	bool upperhalf = false;
	bool gender = true;
	bool kinecttoggle = true;
	bool eyes = true;
	bool eyebrows = true;
	bool mouth = true;

	HudElement* getHud(u32 id);
	u32         addHud(HudElement* hud);
	HudElement* removeHud(u32 id);
	void        clearHud();
	u32         maxHudId() {
		return hud.size();
	}

	u32 hud_flags;
	s32 hud_hotbar_itemcount;
	std::string hud_hotbar_image;
	std::string hud_hotbar_selected_image;
protected:
	IGameDef *m_gamedef;

	char m_name[PLAYERNAME_SIZE];
	u16 m_breath;
	f32 m_pitch;
	f32 m_yaw;
	v3f m_speed;
	f32 m_kinect_pitch;
	f32 m_kinect_yaw;
	f32 m_kinect_roll;
	f32 m_kinect_left_arm;
	f32	m_kinect_left_shoulder;
	f32 m_kinect_left_arm_OrthoX;
	f32 m_kinect_left_arm_OrthoZ;
	f32	m_kinect_right_arm;
	f32	m_kinect_right_shoulder;
	int	m_kinect_face;
	int m_puredata_face;
	f32 m_kinect_right_arm_OrthoX;
	f32 m_kinect_right_arm_OrthoZ;
	f32 m_kinect_left_leg;
	f32 m_kinect_left_leg_OrthoX;
	f32 m_kinect_left_leg_OrthoZ;
	f32	m_kinect_right_leg;
	f32 m_kinect_right_leg_OrthoX;
	f32 m_kinect_right_leg_OrthoZ;
	f32	m_kinect_torso_rot;
	f32 m_kinect_torso_roll;
	f32 m_kinect_torso_x;
	f32 m_kinect_left_shoulder_flag;
	f32 m_kinect_right_shoulder_flag;
	int m_pdata_face;
	int m_gender_flag;
	aabb3f m_collisionbox;

	bool m_dirty;

	std::vector<HudElement *> hud;

	std::string m_sky_type;
	video::SColor m_sky_bgcolor;
	std::vector<std::string> m_sky_params;

	bool m_day_night_ratio_do_override;
	float m_day_night_ratio;
private:
	// Protect some critical areas
	// hud for example can be modified by EmergeThread
	// and ServerThread
	Mutex m_mutex;
};


/*
	Player on the server
*/
class RemotePlayer : public Player
{
public:
	RemotePlayer(IGameDef *gamedef, const char *name);
	virtual ~RemotePlayer() {}

	void save(std::string savedir);

	PlayerSAO *getPlayerSAO()
	{ return m_sao; }
	void setPlayerSAO(PlayerSAO *sao)
	{ m_sao = sao; }
	void setPosition(const v3f &position);

private:
	PlayerSAO *m_sao;
};

#endif

