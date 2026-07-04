
package gaming;



public class Maintenance {
    private int malfId;          // رقم العطل المرجعي
    private int itemId;          // رقم الجهاز المعطل
    private String description;  // وصف المشكلة (مثلاً: عطل في أزرار اليد)
    private int fixedIs;         // حالة الإصلاح: 0 = لم يصلح، 1 = تم الإصلاح


    public Maintenance(int malfId, int itemId, String description, int fixedIs) {
        this.malfId = malfId;
        this.itemId = itemId;
        this.description = description;
        this.fixedIs = fixedIs;
    }


    public int getMalfId() { return malfId; }
    public void setMalfId(int malfId) { this.malfId = malfId; }

    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getFixedIs() { return fixedIs; }
    public void setFixedIs(int fixedIs) { this.fixedIs = fixedIs; }
}
