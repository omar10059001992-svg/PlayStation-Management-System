package gaming;

/**
 * استثناء مخصص (Custom Exception) يُستخدم لإدارة ومعالجة الأخطاء والانتهاكات
 * الخاصة بالقيود البرمجية أو عمليات قاعدة البيانات داخل النظام.


 */
public class SystemDatabaseException extends Exception {

    /**
     * ينشئ استثناءً جديداً مع رسالة خطأ تفصيلية تشرح سبب المشكلة أو الانتهاك.
     * * @param message نص رسالة الخطأ التي تظهر للمستخدم أو تُسجل في النظام.
     */
    public SystemDatabaseException(String message) {
        super(message);
    }
}