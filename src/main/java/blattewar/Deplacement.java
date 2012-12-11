package blattewar;
public class Deplacement implements Action
{
    private Direction direction = null;
    public Deplacement()
    {}
    public Deplacement(Direction dir)
    {
        this.direction = dir;
    }
    public Direction getDirection()
    {
        return this.direction;
    }
    public void setDirection(Direction dir)
    {
        this.direction = dir;
    }
}
