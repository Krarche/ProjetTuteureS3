package Logic;

import static Logic.Type.MELEE;
import java.util.ArrayList;
import Maths.Vector2f;

public class EntityEnemy extends EntityDynamic {
    public Type m_type;
    public boolean m_orientation; //True means "look to the right"

    /**
    * Default constructor
    */
    public EntityEnemy() {
        super();
        m_type = MELEE;
        m_orientation = true;
		m_name="Batard!";
    }

    /**
    * Complete constructor
    */
    public EntityEnemy(Type m_type, Vector2f m_size, boolean m_orientation) {
        super();        
        this.m_type= m_type;
        this.m_orientation = m_orientation;
    }

	@Override
    public void update() {
		super.update();
        rotate();
        move();
		
    }
    
    public void rotate() {
        if(m_pos.x > Logic.getPlayer().m_pos.x && m_orientation == true) {
            m_orientation = false;
        }
        else if (m_pos.x < Logic.getPlayer().m_pos.x && m_orientation == false) {
            m_orientation = true;
        }
    }
    public void move() {
        switch(m_type){
            case MELEE :
				if(Logic.getPlayer().getPos().y > m_pos.y+Logic.getPlayer().getSize().y)
					jump();
				if(Math.abs(Logic.getPlayer().getPos().x-m_pos.x)>3f) {
					if(m_orientation) {
						setSpeed(0.05f, m_speed.y);
					}
					else {
						setSpeed(-0.05f, m_speed.y);
					}
					break;
				}
            case RANGED :
				shoot(Logic.getPlayer().getPos());
                break;
            default :
                break;
        }
    }
	public static EntityEnemy create() {
		EntityEnemy temp=new EntityEnemy();
		all.add(temp);
		EntityDynamic.getAll().add(temp);
		Entity.getAll().add(temp);
		return temp;
	}
	public static ArrayList getAll() {
		return all;
	}
	private static ArrayList<EntityEnemy> all=new ArrayList();
}