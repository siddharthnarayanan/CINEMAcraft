package net.minecraft.src;
/**
 * Creates the helmet for recording (OFF)
 * @author Cody Cahoon
 * @version 2013.10.22
 *
 */
public class BlockInvisible extends Block {

	/**
	 * Icon for the front of the block/head
	 */
	private Icon front;
	/**
	 * Icon for the back of the block/head
	 */
	private Icon back;
	/**
	 * Icon for the top of the block/head
	 */
	private Icon top;
	/**
	 * Icon for the bottom of the block/head
	 */
	private Icon bottom;
	/**
	 * Icon for the left side of the block/head
	 */
	private Icon left;
	/**
	 * Icon for the right side of the block/head
	 */
	private Icon right;
	

	/**
	 * The super call to the Block class
	 * @param par1 The ID given in mod_OperaCraft.class
	 * @param par2Material The material given in the mod_OperaCraft.class
	 * @param num Which face to display/register
	 */		
	protected BlockInvisible(int par1, Material par2Material){
		super(par1, par2Material);
	}

	/**
	 * From the specified side and block metadata retrieves the blocks texture.
	 * Args: side, metadata
	 */
	public Icon getBlockTextureFromSideAndMetadata(int par1, int par2) {
		switch (par1) {
		case 1:
			return top;
		case 2:
			return back;
		case 3:
			return front;
		case 4:
			return right;
		case 5:
			return left;
		default:
			return bottom;
		}
	}

	/**
	 * Gets the Icon for the block
	 * @param par1 which side
	 * @param par2 the metadata
	 * @return the side called
	 */
	public Icon getIcon(int par1, int par2)
	{
		switch (par1) {
		case 1:
			return top;
		case 2:
			return back;
		case 3:
			return front;
		case 4:
			return right;
		case 5:
			return left;
		default:
			return bottom;
		}	
	}
	
	/**
	 * When this method is called, your block should register all the icons it
	 * needs with the given IconRegister. This is the only chance you get to
	 * register icons.
	 */
	public void registerIcons(IconRegister par1IconRegister) {
		this.front = par1IconRegister.registerIcon("invisibleBlock");
		this.back = par1IconRegister.registerIcon("invisibleBlock");
		this.top = par1IconRegister.registerIcon("invisibleBlock");
		this.bottom = par1IconRegister.registerIcon("invisibleBlock");
		this.left = par1IconRegister.registerIcon("invisibleBlock");
		this.right = par1IconRegister.registerIcon("invisibleBlock");
	}
}
