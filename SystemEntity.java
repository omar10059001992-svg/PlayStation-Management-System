
package gaming;


public abstract class SystemEntity implements DBOperations {

    private int id; 

    public SystemEntity(int id) {
        this.id = id;
    }


    public int getId() { 
        return id; 
    }
    
    public void setId(int id) { 
        this.id = id; 
    }


    protected void applyLibyanStandardVariableConstraint(String fieldName, Object value) throws SystemDatabaseException {
        if (value == null || value.toString().trim().isEmpty()) {
            throw new SystemDatabaseException("انتهاك القيود القياسية (ISO-99999): الحقل '" + fieldName + "' لا يمكن أن يكون فارغاً.");
        }
    }
}