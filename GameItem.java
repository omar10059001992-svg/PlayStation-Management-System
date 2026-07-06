package gaming;

/**
 * الكلاس الفرعي (Subclass) الذي يمثل الأجهزة أو العناصر المتاحة للعب في الصالة (مثل بلايستيشن 5، VR، إلخ).
 * يرث هذا الكلاس من {@link SystemEntity} ويتحقق من القيود المالية والبيانات الخاصة بالأجهزة.
 */
public class GameItem extends SystemEntity {

    /** اسم الجهاز أو العنصر */
    private String name;

    /** نوع الجهاز (مثلاً: Console, VR, Arcade) */
    private String type;

    /** سعر تكلفة الساعة الواحدة لتأجير الجهاز أو اللعب به */
    private double hourlyRate;

    /** حالة الجهاز الحالية (مثلاً: Available, Occupied, Maintenance) */
    private String status;

    /**
     * ينشئ عنصراً أو جهازاً جديداً بكامل تفاصيله.
     * * @param id المعرف الفريد للجهاز (يتم تمريره للكلاس الأب).
     * @param name اسم الجهاز.
     * @param type نوع الجهاز.
     * @param hourlyRate سعر الساعة.
     * @param status حالة الجهاز.
     */
    public GameItem(int id, String name, String type, double hourlyRate, String status) {
        super(id);
        this.name = name;
        this.type = type;
        this.hourlyRate = hourlyRate;
        this.status = status;
    }

    /**
     * استرجاع اسم الجهاز.
     * @return اسم الجهاز الحالي.
     */
    public String getName() { return name; }

    /**
     * تحديث اسم الجهاز.
     * @param name اسم الجهاز الجديد.
     */
    public void setName(String name) { this.name = name; }

    /**
     * استرجاع نوع الجهاز.
     * @return نوع الجهاز.
     */
    public String getType() { return type; }

    /**
     * تحديث نوع الجهاز.
     * @param type النوع الجديد للجهاز.
     */
    public void setType(String type) { this.type = type; }

    /**
     * استرجاع سعر الساعة للجهاز.
     * @return سعر الساعة الحالي.
     */
    public double getHourlyRate() { return hourlyRate; }

    /**
     * تحديث سعر الساعة للجهاز.
     * @param hourlyRate سعر الساعة الجديد.
     */
    public void setHeaderRate(double hourlyRate) { this.hourlyRate = hourlyRate; }

    /**
     * استرجاع حالة الجهاز الحالية.
     * @return حالة الجهاز (مثل متاح أو مشغول).
     */
    public String getStatus() { return status; }

    /**
     * تحديث حالة الجهاز.
     * @param status الحالة الجديدة المراد تعيينها.
     */
    public void setStatus(String status) { this.status = status; }

    /**
     * يتحقق من صحة القيود الخاصة بالجهاز.
     * يضمن عدم ترك الحقول النصية فارغة، ويتحقق من أن سعر الساعة قيمة موجبة تماماً أكبر من صفر.
     * * @throws SystemDatabaseException في حال انتهاك أي من قيود الحقول النصية أو إذا كان السعر صفر أو أقل.
     */
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