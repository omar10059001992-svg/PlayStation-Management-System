package gaming;

/**
 * الكلاس المسؤول عن إدارة عمليات الصيانة والأعطال الخاصة بالأجهزة في الصالة.
 * يتابع المشاكل التقنية المسجلة لكل جهاز وحالة الإصلاح الخاصة بها.
 */
public class Maintenance {

    /** الرقم المرجعي الفريد لطلب أو سجل الصيانة (Malfunction ID) */
    private int malfId;

    /** الرقم التعريفي للجهاز المعطل المرتبط بـ {@link GameItem} */
    private int itemId;

    /** وصف تفصيلي للمشكلة التقنية (مثلاً: عطل في أزرار يد التحكم) */
    private String description;

    /** حالة الإصلاح الحالية: (0 تعني لم يتم الإصلاح، 1 تعني تم الإصلاح بنجاح) */
    private int fixedIs;

    /**
     * ينشئ سجل صيانة جديد بكامل تفاصيله.
     * * @param malfId الرقم المرجعي الفريد للعطل.
     * @param itemId رقم الجهاز المعطل.
     * @param description وصف العطل المكتشف.
     * @param fixedIs حالة الإصلاح (0 أو 1).
     */
    public Maintenance(int malfId, int itemId, String description, int fixedIs) {
        this.malfId = malfId;
        this.itemId = itemId;
        this.description = description;
        this.fixedIs = fixedIs;
    }

    /**
     * استرجاع الرقم المرجعي للعطل.
     * @return رقم العطل الحالي.
     */
    public int getMalfId() { return malfId; }

    /**
     * تحديث الرقم المرجعي للعطل.
     * @param malfId رقم العطل الجديد.
     */
    public void setMalfId(int malfId) { this.malfId = malfId; }

    /**
     * استرجاع رقم الجهاز المرتبط بالصيانة.
     * @return رقم الجهاز (Item ID).
     */
    public int getItemId() { return itemId; }

    /**
     * ربط سجل الصيانة بجهاز معين عبر رقمه.
     * @param itemId رقم الجهاز المعطل.
     */
    public void setItemId(int itemId) { this.itemId = itemId; }

    /**
     * استرجاع وصف العطل المكتوب.
     * @return نص وصف المشكلة.
     */
    public String getDescription() { return description; }

    /**
     * تحديث أو تعديل وصف العطل.
     * @param description الوصف الجديد للمشكلة.
     */
    public void setDescription(String description) { this.description = description; }

    /**
     * استرجاع حالة الإصلاح الحالية.
     * @return قيمة الحالة (0 لغير المصلح، 1 للمصلح).
     */
    public int getFixedIs() { return fixedIs; }

    /**
     * تحديث حالة عملية الصيانة (تم الإصلاح أم لا).
     * @param fixedIs الحالة الجديدة (0 أو 1).
     */
    public void setFixedIs(int fixedIs) { this.fixedIs = fixedIs; }
}