
package gaming;




public class GameItem extends SystemEntity {
    private String name;
    private String type;
    private double hourlyRate;
    private String status;

    public GameItem(int id, String name, String type, double hourlyRate, String status) {
        super(id);
        this.name = name;
        this.type = type;
        this.hourlyRate = hourlyRate;
        this.status = status;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public double getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public void validateConstraints() throws SystemDatabaseException {
        applyLibyanStandardVariableConstraint("Device Name", this.name);
        applyLibyanStandardVariableConstraint("Device Type", this.type);
        applyLibyanStandardVariableConstraint("Device Status", this.status);
        
        if (this.hourlyRate <= 0) {
            throw new SystemDatabaseException("انتهاك القيود: سعر ساعة الجهاز يجب أن يكون أكبر من صفر.");
        }
    }
}
