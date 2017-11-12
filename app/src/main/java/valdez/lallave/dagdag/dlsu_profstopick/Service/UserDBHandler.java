package valdez.lallave.dagdag.dlsu_profstopick.Service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import valdez.lallave.dagdag.dlsu_profstopick.Beans_Model.Admin;
import valdez.lallave.dagdag.dlsu_profstopick.Beans_Model.Student;
import valdez.lallave.dagdag.dlsu_profstopick.Beans_Model.Teacher;

public class UserDBHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "ProfsToPick";

    // Contacts table name
    private static final String TABLE_STUDENT = "student";
    private static final String TABLE_ADMIN   = "admin"  ;
    private static final String TABLE_TEACHER   = "teacher";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_HASHED_PASS = "hashedPass";
    private static final String KEY_TEACHER_NAME = "name";
    private static final String KEY_TEACHER_DEPARTMENT = "college";

    public UserDBHandler(Context contex) {
        super(contex, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        System.out.println("CREATING");
        String CREATE_STUDENT_DETAIL_TABLE = "CREATE TABLE " + TABLE_STUDENT + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_EMAIL + " TEXT,"
                + KEY_HASHED_PASS + " TEXT " + ")";

        String CREATE_ADMIN_TABLE = "CREATE TABLE " + TABLE_ADMIN + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_EMAIL + " TEXT,"
                + KEY_HASHED_PASS + " TEXT " + ")";

        String CREATE_TEACHER_TABLE = "CREATE TABLE " + TABLE_TEACHER + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TEACHER_NAME + " TEXT,"
                + KEY_TEACHER_DEPARTMENT + " TEXT " + ")";

        db.execSQL(CREATE_STUDENT_DETAIL_TABLE);
        db.execSQL(CREATE_ADMIN_TABLE);
        db.execSQL(CREATE_TEACHER_TABLE);
        System.out.println("TEACHER TABLE CREATED!!!!");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADMIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEACHER);

        // Create tables again
        onCreate(db);
    }


    public void addNewTeacher(Teacher newTeacher) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_TEACHER_NAME, newTeacher.getName());
        values.put(KEY_TEACHER_DEPARTMENT, newTeacher.getDepartment());

        db.insert(TABLE_TEACHER, null, values);
        db.close();
    }

    public void addNewAdmin(Admin newAdmin) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_EMAIL, newAdmin.getEmail());
        values.put(KEY_HASHED_PASS, newAdmin.getHashedPass());

        db.insert(TABLE_ADMIN, null, values);
        db.close();
    }

    public void addNewStudent(Student newStud) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_EMAIL, newStud.getEmail());
        values.put(KEY_HASHED_PASS, newStud.getHashedPass());


        // Inserting Row
        db.insert(TABLE_STUDENT, null, values);
        db.close(); // Closing database connection
    }


    public boolean updateTeachernInfo(Teacher updatedTeacher) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues args = new ContentValues();

        args.put(KEY_TEACHER_NAME, updatedTeacher.getName());
        args.put(KEY_TEACHER_DEPARTMENT, updatedTeacher.getDepartment());

        return db.update(TABLE_TEACHER, args, KEY_ID + "=" + updatedTeacher.getTeacherId(), null) > 0;
    }

    public boolean updateAdminInfo(Admin updatedAdmin) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues args = new ContentValues();

        args.put(KEY_EMAIL, updatedAdmin.getEmail());
        args.put(KEY_HASHED_PASS, updatedAdmin.getHashedPass());

        return db.update(TABLE_ADMIN, args, KEY_ID + "=" + updatedAdmin.getAdminId(), null) > 0;
    }

    public boolean updateStudentInfo(Student updatedStudent) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues args = new ContentValues();

        args.put(KEY_EMAIL, updatedStudent.getEmail());
        args.put(KEY_HASHED_PASS, updatedStudent.getHashedPass());

        return db.update(TABLE_STUDENT, args, KEY_ID + "=" + updatedStudent.getStudentId(), null) > 0;
    }


    public boolean deleteTeacher(int delID){
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(TABLE_TEACHER, KEY_ID + "=" + delID, null) > 0;
    }

    public boolean deleteAdmin(int delID){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_ADMIN, KEY_ID + "=" + delID, null) > 0;
    }

    public boolean deleteStudent(int delID){

        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_STUDENT, KEY_ID + "=" + delID, null) > 0;

    }


    public Boolean validateAdmin(String email, String hashedPass) {

        String selectQuery = "SELECT  * FROM " + TABLE_ADMIN +" WHERE email = '" + email +
                             "' and hashedPass = '" + hashedPass + "';";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        return cursor.moveToFirst();
    }

    public Boolean validateStudent(String email, String hashedPass) {

        String selectQuery = "SELECT  * FROM " + TABLE_STUDENT +" WHERE email = '" + email +
                             "' and hashedPass = '" + hashedPass + "';";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        return cursor.moveToFirst();
    }


    public List<Teacher> getAllTeachers() {

        List<Teacher> teacherList = new ArrayList<Teacher>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TEACHER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                Teacher teacher = new Teacher();
                teacher.setTeacherId(Integer.parseInt(cursor.getString(0)));
                teacher.setName(cursor.getString(1));
                teacher.setDepartment(cursor.getString(2));

                teacherList.add(teacher);

            } while (cursor.moveToNext());
        }

        return teacherList;
    }


    public List<Admin> getAllAdmin() {

        List<Admin> adminList = new ArrayList<Admin>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ADMIN;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                Admin admin = new Admin();
                admin.setAdminId(Integer.parseInt(cursor.getString(0)));
                admin.setEmail(cursor.getString(1));
                admin.setHashedPass(cursor.getString(2));

                adminList.add(admin);

            } while (cursor.moveToNext());
        }

        return adminList;
    }

    public List<Student> getAllStudents() {

        List<Student> studentList = new ArrayList<Student>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_STUDENT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                Student stdnt = new Student();
                stdnt.setStudentId(Integer.parseInt(cursor.getString(0)));
                stdnt.setEmail(cursor.getString(1));
                stdnt.setHashedPass(cursor.getString(2));

                studentList.add(stdnt);

            } while (cursor.moveToNext());
        }

        // return contact list
        return studentList;
    }

    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "message" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);

            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){
            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }
    }
}
