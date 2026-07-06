package gaming;

/**
 * واجهة برمجية (Interface) تحدد العمليات الأساسية الإلزامية الخاصة بالتحقق
 * من البيانات والقيود قبل التفاعل مع قاعدة البيانات أو النظام.
 
 */
public interface DBOperations {

    /**
     * يفرض على الكلاسات التي تطبق هذه الواجهة كتابة منطق برمي مخصص
     * للتحقق من صحة المدخلات وسلامة القيود (Constraints).
     * * @throws SystemDatabaseException في حال وجود انتهاك للقيود القياسية للنظام.
     */
    void validateConstraints() throws SystemDatabaseException;
}