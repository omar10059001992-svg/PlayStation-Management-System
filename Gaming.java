package gaming;

/**
 * نقطة الانطلاق الرئيسية (Application Entry Point) لنظام صالة ألعاب سكواد قيم.
 * يحتوي هذا الكلاس على الدالة {@link #main(String[])} المسؤولة عن تشغيل الواجهة الرسومية
 * وضمان تحميلها بشكل آمن داخل مسار معالجة الأحداث (Event Dispatch Thread).
 *
 */
public class Gaming {

    /**
     * الدالة الأساسية (Main Method) التي يبدأ منها تنفيذ البرنامج بالكامل.
     *
     * @param args مصفوفة المعاملات الممررة عبر سطر الأوامر (Command Line Arguments).
     */
    public static void main(String[] args) {
        // تشغيل الواجهة الرسومية في مسار آمن (Thread-Safe) لمنع تجميد النظام
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                // إنشاء نسخة من النظام وجعلها مرئية للمستخدم
                new SquadGameSystem().setVisible(true);
            }
        });
    }
}