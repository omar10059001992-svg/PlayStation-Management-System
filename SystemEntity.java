package gaming;

/**
 * الكلاس الأساسي (Abstract Base Class) الذي يمثل كياناً عاماً في النظام.
 * يوفر الخصائص الأساسية المشتركة لجميع الكيانات ويفرض تنفيذ عمليات قاعدة البيانات
 * عبر الواجهة {@link DBOperations}.
 * * @author [اسمك]
 * @version 1.0
 */
public abstract class SystemEntity implements DBOperations {

    /** المعرف الفريد للكيان */
    private int id;

    /**
     * ينشئ كياناً جديداً بمعرف محدد.
     * @param id المعرف الفريد للكيان.
     */
    public SystemEntity(int id) {
        this.id = id;
    }

    /**
     * استرجاع المعرف الفريد للكيان.
     * @return قيمة الـ ID.
     */
    public int getId() {
        return id;
    }

    /**
     * تحديث المعرف الفريد للكيان.
     * @param id المعرف الجديد المراد تعيينه.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * يقوم بالتحقق من صحة المدخلات وفقاً للقيود القياسية للنظام.
     * إذا كان الحقل فارغاً أو قيمته غير صالحة، يتم إطلاق استثناء {@link SystemDatabaseException}.
     * * @param fieldName اسم الحقل المراد التحقق منه.
     * @param value القيمة المراد التحقق منها.
     * @throws SystemDatabaseException في حال انتهاك قيود النظام.
     */
    protected void applyLibyanStandardVariableConstraint(String fieldName, Object value) throws SystemDatabaseException {
        if (value == null || value.toString().trim().isEmpty()) {
            throw new SystemDatabaseException("انتهاك القيود القياسية (ISO-99999): الحقل '" + fieldName + "' لا يمكن أن يكون فارغاً.");
        }
    }
}