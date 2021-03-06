package liquibase.change;

import liquibase.servicelocator.PrioritizedService;
import liquibase.structure.DatabaseObject;

import java.util.*;

/**
 * Static metadata about a {@code link Change}.
 * This data is kept in a separate class to better separate the information about the change and the fields of the change.
 * It also ensures there will be no conflict between Change fields and metadata, such as "name".
 * ChangeMetaData instances are immutable.
 *
 * @see ChangeParameterMetaData
 */
public class ChangeMetaData implements PrioritizedService {
    public static final int PRIORITY_DEFAULT = 1;

    private String name;
    private String description;
    private int priority;

    private Map<String, ChangeParameterMetaData> parameters;
    private Set<String> appliesTo;

    public ChangeMetaData(String name, String description, int priority, String[] appliesTo, Map<String, ChangeParameterMetaData> parameters) {
        if (parameters == null) {
            parameters  = new HashMap<String, ChangeParameterMetaData>();
        }
        if (appliesTo != null && appliesTo.length == 0) {
            appliesTo  = null;
        }
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.parameters = Collections.unmodifiableMap(parameters);

        this.appliesTo = null;
        if (appliesTo != null && appliesTo.length > 0) {
            this.appliesTo = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(appliesTo)));
        }
    }

    /**
     * Return the name of the change used to identify it. The name must not contain spaces since it will be used as tag names in XML etc.
     */
    public String getName() {
        return name;
    }

    /**
     * A description of the Change for documentation purposes.
     */
    public String getDescription() {
        return description;
    }

    /**
     * The priority of a Change implementation controls which implementation is used if several exist for the same "name".
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Return the parameters of this Change. Will never return a null map, only an empty or populated map.
     */
    public Map<String, ChangeParameterMetaData> getParameters() {
        return parameters;
    }

    /**
     * Returns the types of DatabaseObjects this change would apply to.
     * Useful for documentation or integrations that present a user with what change commands are available for a given database type.
     * If no information is known, returns null. Will never return an empty set, only null or a populated set.
     * The string value will correspond to the values returned by {@link liquibase.structure.DatabaseObject#getObjectTypeName()}
     */
    public Set<String> getAppliesTo() {
        return appliesTo;
    }

    public boolean appliesTo(DatabaseObject databaseObject) {
        return appliesTo != null && appliesTo.contains(databaseObject.getObjectTypeName());
    }
}
