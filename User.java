package gaming;

/**
 * الكلاس الفرعي (Subclass) الذي يمثل مستخدمي النظام (المشرفين والموظفين).
 * يرث هذا الكلاس من {@link SystemEntity} ويقوم بتطبيق القيود البرمجية الخاصة به.
 * * @author [اسمك]
 * @version 1.0
 */
public class User extends SystemEntity {

    /** اسم المستخدم لتسجيل الدخول */
    private String username;

    /** كلمة المرور الخاصة بالمستخدم */
    private String password;

    /** دور المستخدم في النظام وتطابق القيم في قاعدة البيانات ('Admin' أو 'Employee') */
    private String role;

    /**
     * ينشئ مستخدماً جديداً بكامل تفاصيله.
     * * @param id المعرف الفريد للمستخدم (يتم تمريره للكلاس الأب).
     * @param username اسم المستخدم.
     * @param password كلمة المرور.
     * @param role صلاحية المستخدم ('Admin' أو 'Employee').
     */
    public User(int id, String username, String password, String role) {
        super(id);
        this.username = username;
        this.password = password;
        this.role = role;
    }

    /**
     * استرجاع اسم المستخدم.
     * @return اسم المستخدم الحالي.
     */
    public String getUsername() { return username; }

    /**
     * تحديث اسم المستخدم.
     * @param username اسم المستخدم الجديد.
     */
    public void setUsername(String username) { this.username = username; }

    /**
     * استرجاع كلمة المرور.
     * @return كلمة المرور الحالية.
     */
    public String getPassword() { return password; }

    /**
     * تحديث كلمة المرور.
     * @param password كلمة المرور الجديدة.
     */
    public void setPassword(String password) { this.password = password; }

    /**
     * استرجاع دور وصلاحية المستخدم.
     * @return الصلاحية الحالية ('Admin' أو 'Employee').
     */
    public String getRole() { return role; }

    /**
     * تحديث دور وصلاحية المستخدم.
     * @param role الصلاحية الجديدة المراد تعيينها.
     */
    public void setRole(String role) { this.role = role; }

    /**
     * يتحقق من القيود الخاصة ببيانات المستخدم (اسم المستخدم وكلمة المرور).
     * يضمن عدم ترك هذه الحقول فارغة عبر استدعاء ميثود التحقق من الكلاس الأب.
     * * @throws SystemDatabaseException في حال كان اسم المستخدم أو كلمة المرور فارغة.
     */
    @Override
    public void validateConstraints() throws SystemDatabaseException {
        applyLibyanStandardVariableConstraint("Username", this.username);
        applyLibyanStandardVariableConstraint("Password", this.password);
    }
}