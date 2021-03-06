package de.hochschuletrier.gdw.ss14.ecs;

import com.badlogic.gdx.utils.*;
import de.hochschuletrier.gdw.ss14.ecs.components.*;
import de.hochschuletrier.gdw.ss14.game.*;

import java.util.*;

/**
 * Created by Daniel Dreher on 30.09.2014.
 */
public class EntityManager
{
    public static EntityManager instance;

    private int currentId = 0; // next unused ID
    private List<Integer> entityList;
    private HashMap<Class, HashMap<Integer, ? extends Component>> componentStorage;

    private EntityManager()
    {
        entityList = new LinkedList<Integer>();
        componentStorage = new HashMap<Class, HashMap<Integer, ? extends Component>>();
    }

    public static EntityManager getInstance()
    {
        if (instance == null)
        {
            instance = new EntityManager();
        }

        return instance;
    }

    public int generateNewEntityID()
    {
        // TODO: maybe use java UUIDs?
        if (currentId < Integer.MAX_VALUE)
        {
            return currentId++;
        }
        else // all IDs taken
        {
            // check if there's a free id
            for (int i = 1; i < Integer.MAX_VALUE; i++)
            {
                if (!entityList.contains(i))
                    return i;
            }

            // else throw error!
            throw new Error("All entity IDs taken!");
        }
    }

    public int createEntity()
    {
        int newID = generateNewEntityID();

        if (newID < 0)
        {
            // should never be the case! else something somewhere goes wrong :s
            throw new Error("Generating Entity-ID caused bufferoverflow!");
        }
        else
        {
            entityList.add(newID);
            return newID;
        }
    }

    public void deleteEntity(Integer entity) // argument passed as an object to remove key (else removes index!!)
    {
        entityList.remove(entity);

        for (HashMap<Integer, ? extends Component> store : componentStorage.values())
        {
            store.remove(entity);
        }
    }

    public void deleteAllDogEntities()
    {
        Array<Integer> dogs = getAllEntitiesWithComponents(EnemyComponent.class);

        if(Game.behaviourManager != null)
        {
            Game.behaviourManager.deleteAllBehaviours();
        }

        for (Integer dog : dogs)
        {
            deletePhysicEntity(dog);
        }
    }

    public void deleteAllCatBoxEntities()
    {
        Array<Integer> catBoxes = getAllEntitiesWithComponents(CatBoxComponent.class);

        for (Integer catBox : catBoxes)
        {
            deletePhysicEntity(catBox);
        }
    }

    public void deleteAllWoolEntities()
    {
        Array<Integer> catBoxes = getAllEntitiesWithComponents(WoolPropertyComponent.class);

        for (Integer catBox : catBoxes)
        {
            deletePhysicEntity(catBox);
        }
    }

    public void deleteAllStairs()
    {
        Array<Integer> stairs = getAllEntitiesWithComponents(StairComponent.class);

        for (Integer stair : stairs)
        {
            deletePhysicEntity(stair);
        }
    }

    public void deletePhysicEntity(int entity)
    {
        PhysicsComponent physicsComponent = getComponent(entity, PhysicsComponent.class);

        if(physicsComponent != null)
        {
            // set flaggedForRemoval so a system can clean up all the bodies and then delete the entity.
            physicsComponent.flaggedForRemoval = true;
        }
    }

    public void deleteAllGameplayRelatedEntitiesExcludingCat()
    {
        deleteAllDogEntities();;
        deleteAllCatBoxEntities();
        deleteAllWoolEntities();
        deleteAllStairs();
    }

    public void deleteAllEntities()
    {
        entityList.clear();

        for (HashMap<Integer, ? extends Component> store : componentStorage.values())
        {
            store.clear();
        }
    }

    public <T extends Component> void addComponent(int entity, T component)
    {
        Class classToAdd;

        if (component instanceof PhysicsComponent)
        {
            classToAdd = PhysicsComponent.class;
        }
        else
        {
            classToAdd = component.getClass();
        }

        HashMap<Integer, ? extends Component> storage = componentStorage.get(classToAdd);

        // there's no key set yet for the given component, create a new key
        if (storage == null)
        {
            storage = new HashMap<Integer, T>();
            componentStorage.put(classToAdd, storage);
        }

        ((HashMap<Integer, T>) storage).put(entity, component);
    }


    public <T extends Component> T getComponent(int entity, Class<T> componentType)
    {
        T result = null;
        HashMap<Integer, ? extends Component> storage = componentStorage.get(componentType);

        if (storage != null && storage.size() > 0)
        {
            result = (T) storage.get(entity);
        }

        return result;
    }

    public <T extends Component> List<T> getAllComponentsOfType(Class<T> componentType)
    {
        HashMap<Integer, ? extends Component> store = componentStorage.get(componentType);

        if (store == null)
        {
            return new LinkedList<T>();
        }
        else
        {
            return new ArrayList<T>((Collection<T>) store.values());
        }
    }


    public <T extends Component> List<T> getAllComponentsOfType(int entity, Class<T> componentType)
    {
        List<T> allComponents = getAllComponentsOfEntity(entity);
        LinkedList<T> returnList = new LinkedList<>();

        // check which elements of allComponents are instances of the class componentType
        for (T t : allComponents)
        {
            if (t.getClass() == componentType || t.getClass().getSuperclass() == componentType)
            {
                returnList.addLast(t);
            }
        }

        return returnList;
    }

    public <T extends Component> List<T> getAllComponentsOfEntity(int entity)
    {
        LinkedList<T> components = new LinkedList<T>();

        for (HashMap<Integer, ? extends Component> store : componentStorage.values())
        {
            if (store == null)
            {
                continue;
            }

            T component = (T) store.get(entity);

            if (component != null)
            {
                components.addLast(component);
            }
        }
        return components;
    }

    private <T extends Component> Set<Integer> getAllEntitiesWithComponent(Class<T> componentType)
    {
        HashMap<Integer, ? extends Component> storage = componentStorage.get(componentType);

        if (storage == null)
        {
            return new HashSet<Integer>();
        }

        return storage.keySet();
    }

    public Array<Integer> getAllEntitiesWithComponents(Class... components)
    {
        Array entityArray = new Array();

        // get all entities with the first component
        Set keySet = getAllEntitiesWithComponent(components[0]);

        // then iterate over these entities to check if they got all the other components
        Iterator it = keySet.iterator();

        while (it.hasNext())
        {
            boolean hasAllComponents = true;
            Integer entity = (Integer) it.next();

            for (int i = 1; i < components.length; ++i)
            {
                if (getComponent(entity, components[i]) == null)
                {
                    hasAllComponents = false;
                    break;
                }
            }

            if (hasAllComponents)
            {
                entityArray.add(entity);
            }
        }

        return entityArray;

    }

    public <T extends Component> void removeComponent(Integer entity, T component)
    {
        Class classToRemove;

        if (component instanceof PhysicsComponent)
        {
            classToRemove = component.getClass().getSuperclass();
        }
        else
        {
            classToRemove = component.getClass();
        }

        HashMap<Integer, ? extends Component> store = componentStorage.get(classToRemove);
        if (store == null)
        {
            return;
        }

        T result = (T) store.remove(entity);
    }

}
