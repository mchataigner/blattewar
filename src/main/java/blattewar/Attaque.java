package blattewar;
public class Attaque implements Action
{
    private Direction direction = null;
    public Attaque()
    {}
    public Attaque(Direction dir)
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
