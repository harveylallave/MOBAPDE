package valdez.lallave.dagdag.dlsu_profstopick.Service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import valdez.lallave.dagdag.dlsu_profstopick.Beans_Model.Admin;
import valdez.lallave.dagdag.dlsu_profstopick.Beans_Model.Student;

public class UserDBHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "ProfsToPick";

    // Contacts table name
    private static final String TABLE_STUDENT = "student";
    private static final String TABLE_ADMIN = "admin";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_HASHED_PASS = "hashedPass";

    public UserDBHandler(Context contex) {
        super(contex, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_STUDENT_DETAIL_TABLE = "CREATE TABLE " + TABLE_STUDENT + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_EMAIL + " TEXT,"
                + KEY_HASHED_PASS + " TEXT " + ")";

        String CREATE_ADMIN_TABLE = "CREATE TABLE " + TABLE_ADMIN + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_EMAIL + " TEXT,"
                + KEY_HASHED_PASS + " TEXT " + ")";

        db.execSQL(CREATE_STUDENT_DETAIL_TABLE);
        db.execSQL(CREATE_ADMIN_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADMIN);

        // Create tables again
        onCreate(db);
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


    public boolean updateAdminInfo(Admin updatedAdmin) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues args = new ContentValues();

        args.put(KEY_EMAIL, updatedAdmin.getEmail());
        args.put(KEY_HASHED_PASS, updatedAdmin.getHashedPass());

        return db.update(TABLE_STUDENT, args, KEY_ID + "=" + updatedAdmin.getAdminId(), null) > 0;
    }

    public boolean updateStudentInfo(Student updatedStudent) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues args = new ContentValues();

        args.put(KEY_EMAIL, updatedStudent.getEmail());
        args.put(KEY_HASHED_PASS, updatedStudent.getHashedPass());

        return db.update(TABLE_STUDENT, args, KEY_ID + "=" + updatedStudent.getStudentId(), null) > 0;
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


}
