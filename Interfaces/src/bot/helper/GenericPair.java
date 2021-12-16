package bot.helper;

import java.io.Serializable;

public class GenericPair<L, R> implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8206768512374939625L;
	
	private L left;
	private R right;

	public GenericPair(L left, R right)
	{
		this.left = left;
		this.right = right;
	}

	/**
	 * @return the left
	 */
	public L getLeft()
	{
		return left;
	}

	/**
	 * @return the right
	 */
	public R getRight()
	{
		return right;
	}

	public void setLeft(L leftValue)
	{
		this.left = leftValue;
	}

	public void setRight(R rightValue)
	{
		this.right = rightValue;
	}

	public boolean hasNullData()
	{
		return left == null || right == null;
	}

}
