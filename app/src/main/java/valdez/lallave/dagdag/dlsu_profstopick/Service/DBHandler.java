package valdez.lallave.dagdag.dlsu_profstopick.Service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.SyncStateContract;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import valdez.lallave.dagdag.dlsu_profstopick.Beans_Model.Admin;
import valdez.lallave.dagdag.dlsu_profstopick.Beans_Model.Comment;
import valdez.lallave.dagdag.dlsu_profstopick.Beans_Model.Student;
import valdez.lallave.dagdag.dlsu_profstopick.Beans_Model.Suggest;
import valdez.lallave.dagdag.dlsu_profstopick.Beans_Model.Teacher;

public class DBHandler extends SQLiteOpenHelper {
    //FIREBASE DB LINK
    /*     https://mobapde-46925.firebaseio.com/    */
    //Firebase variables
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    final DatabaseReference studentDatabaseReference = databaseReference.child("student");
    final DatabaseReference teacherDatabaseReference = databaseReference.child("teacher");
    final DatabaseReference commentDatabaseReference = databaseReference.child("comment");
    final DatabaseReference adminDatabaseReference = databaseReference.child("admin");

    String key; //Key used to push in the database

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 9;

    // Database Name
    private static final String DATABASE_NAME = "ProfsToPick";

    // Contacts table name
    private static final String TABLE_STUDENT        = "student";
    private static final String TABLE_ADMIN          = "admin"  ;
    private static final String TABLE_TEACHER        = "teacher";
    private static final String TABLE_COMMENT        = "comment";
    private static final String TABLE_FOLLOWING_PROF = "following";
    private static final String TABLE_SUGGEST_PROF   = "suggest";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_HASHED_PASS = "hashedPass";
    private static final String KEY_TEACHER_NAME = "name";
    private static final String KEY_TEACHER_DEPARTMENT = "college";
    private static final String KEY_COMMENT_TITLE = "title";
    private static final String KEY_COMMENT_BODY = "body";
    private static final String KEY_COMMENT_RATING = "rating";
    private static final String KEY_COMMENT_REVIEWER = "reviewer";
    private static final String KEY_COMMENT_TEACHERID = "teacherId";
    private static final String KEY_FOLLOWING_PROF_TEACHERID = "teacherId";
    private static final String KEY_FOLLOWING_PROF_STUDENTID = "studentId";
    private static final String KEY_SUGGEST_PROF_NAME = "name";
    private static final String KEY_SUGGEST_PROF_DEPARTMENT = "department";
    private static final String KEY_SUGGEST_PROF_SUGGESTEDBY = "suggestedby";
    public DBHandler(Context contex) {
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

        String CREATE_TEACHER_TABLE = "CREATE TABLE " + TABLE_TEACHER + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TEACHER_NAME + " TEXT,"
                + KEY_TEACHER_DEPARTMENT + " TEXT " + ")";

        String CREATE_COMMENT_TABLE = "CREATE TABLE " + TABLE_COMMENT + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_COMMENT_TITLE + " TEXT,"
                + KEY_COMMENT_BODY + " TEXT,"
                + KEY_COMMENT_RATING + " FLOAT,"
                + KEY_COMMENT_REVIEWER + " TEXT,"
                + KEY_COMMENT_TEACHERID + " INTEGER " + ")";

        String CREATE_FOLLOWING_PROF_TABLE = "CREATE TABLE " + TABLE_FOLLOWING_PROF + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_FOLLOWING_PROF_STUDENTID + " INTEGER ,"
                + KEY_FOLLOWING_PROF_TEACHERID + " INTEGER " + ")";

        String CREATE_SUGGEST_PROF_TABLE = "CREATE TABLE " + TABLE_SUGGEST_PROF + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_SUGGEST_PROF_NAME + " TEXT,"
                + KEY_SUGGEST_PROF_DEPARTMENT + " TEXT,"
                + KEY_SUGGEST_PROF_SUGGESTEDBY + " TEXT" + ")";

        db.execSQL(CREATE_STUDENT_DETAIL_TABLE);
        db.execSQL(CREATE_ADMIN_TABLE);
        db.execSQL(CREATE_TEACHER_TABLE);
        db.execSQL(CREATE_COMMENT_TABLE);
        db.execSQL(CREATE_FOLLOWING_PROF_TABLE);
        db.execSQL(CREATE_SUGGEST_PROF_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADMIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEACHER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOLLOWING_PROF);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUGGEST_PROF);

        // Create tables again
        onCreate(db);
    }

    public void addSuggestProf(Suggest s) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_SUGGEST_PROF_NAME, s.getName());
        values.put(KEY_SUGGEST_PROF_DEPARTMENT, s.getDepartment());
        values.put(KEY_SUGGEST_PROF_SUGGESTEDBY, s.getSuggestedBy());

        db.insert(TABLE_SUGGEST_PROF, null, values);
        db.close();
    }

/*    public boolean toggleFollowProf(Student student, Teacher teacher){


        String selectQuery = "SELECT  * FROM " + TABLE_FOLLOWING_PROF +" WHERE studentId = '" + student.getStudentId() +
                             "' and teacherId = '" + teacher.getTeacherId() + "';";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) { // Unfollowing prof -> removing from the db
            int followingId = cursor.getInt(0);
            deleteFollowProf(followingId);
            return false;
        } else {    // Following prof -> adding to the db
            addFollowProf(student, teacher);
            return true;
        }

    }*/

/*    public void addFollowProf(Student student, Teacher teacher) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_FOLLOWING_PROF_STUDENTID, student.getStudentId());
        values.put(KEY_FOLLOWING_PROF_TEACHERID, teacher.getTeacherId());

        db.insert(TABLE_FOLLOWING_PROF, null, values);
        db.close();
    }*/

    public void addNewComment(Comment newComment) {
        key = studentDatabaseReference.push().getKey();

        commentDatabaseReference.child(key).setValue(newComment);
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//
//        values.put(KEY_COMMENT_TITLE, newComment.getTitle());
//        values.put(KEY_COMMENT_BODY, newComment.getBody());
//        values.put(KEY_COMMENT_RATING, newComment.getRate());
//        values.put(KEY_COMMENT_REVIEWER, newComment.getReviewer());
//        values.put(KEY_COMMENT_TEACHERID, newComment.getTeacherID());
//
//        db.insert(TABLE_COMMENT, null, values);
//        db.close();
    }

    public void addNewTeacher(Teacher newTeacher) {
        key = teacherDatabaseReference.push().getKey();

        teacherDatabaseReference.child(key).setValue(newTeacher);

//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//
//        values.put(KEY_TEACHER_NAME, newTeacher.getName());
//        values.put(KEY_TEACHER_DEPARTMENT, newTeacher.getDepartment());
//
//        db.insert(TABLE_TEACHER, null, values);
//        db.close();
    }

    public void addNewAdmin(Admin newAdmin) {
        key = adminDatabaseReference.push().getKey();

        adminDatabaseReference.child(key).setValue(newAdmin);
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//
//        values.put(KEY_EMAIL, newAdmin.getEmail());
//        values.put(KEY_HASHED_PASS, newAdmin.getHashedPass());
//
//        db.insert(TABLE_ADMIN, null, values);
//        db.close();
    }

    public void addNewStudent(Student newStud) {
        key = studentDatabaseReference.push().getKey();

        studentDatabaseReference.child(key).setValue(newStud);


//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//
//        values.put(KEY_EMAIL, newStud.getEmail());
//        values.put(KEY_HASHED_PASS, newStud.getHashedPass());
//
//
//        // Inserting Row
//        db.insert(TABLE_STUDENT, null, values);
//        db.close(); // Closing database connection
    }

/*    public boolean updateCommentInfo(Comment updatedComment) {

        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues values = new ContentValues();

        values.put(KEY_COMMENT_TITLE, updatedComment.getTitle());
        values.put(KEY_COMMENT_BODY, updatedComment.getBody());
        values.put(KEY_COMMENT_RATING, updatedComment.getRate());
        values.put(KEY_COMMENT_REVIEWER, updatedComment.getReviewer());
        values.put(KEY_COMMENT_TEACHERID, updatedComment.getTeacherID());


        return db.update(TABLE_COMMENT, values, KEY_ID + "=" + updatedComment.getId(), null) > 0;
    }*/

/*    public boolean updateTeacherInfo(Teacher updatedTeacher) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues args = new ContentValues();

        args.put(KEY_TEACHER_NAME, updatedTeacher.getName());
        args.put(KEY_TEACHER_DEPARTMENT, updatedTeacher.getDepartment());

        return db.update(TABLE_TEACHER, args, KEY_ID + "=" + updatedTeacher.getTeacherId(), null) > 0;
    }*/

/*    public boolean updateAdminInfo(Admin updatedAdmin) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues args = new ContentValues();

        args.put(KEY_EMAIL, updatedAdmin.getEmail());
        args.put(KEY_HASHED_PASS, updatedAdmin.getHashedPass());

        return db.update(TABLE_ADMIN, args, KEY_ID + "=" + updatedAdmin.getAdminId(), null) > 0;
    }*/

/*    public boolean updateStudentInfo(Student updatedStudent) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues args = new ContentValues();

        args.put(KEY_EMAIL, updatedStudent.getEmail());
        args.put(KEY_HASHED_PASS, updatedStudent.getHashedPass());

        return db.update(TABLE_STUDENT, args, KEY_ID + "=" + updatedStudent.getStudentId(), null) > 0;
    }*/


    public boolean deleteComment(int delID){
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(TABLE_COMMENT, KEY_ID + "=" + delID, null) > 0;
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

    public boolean deleteFollowProf(int delID){

        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_FOLLOWING_PROF, KEY_ID + "=" + delID, null) > 0;

    }

   /* public boolean validateFollowingProf(Student student, Teacher teacher) {

        String selectQuery = "SELECT  * FROM " + TABLE_FOLLOWING_PROF +" WHERE studentId = '" + student.getStudentId() +
                "' and teacherId = '" + teacher.getTeacherId() + "';";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        return cursor.moveToFirst();
    }*/

    public boolean validateAdmin(String email, String hashedPass) {

        String selectQuery = "SELECT  * FROM " + TABLE_ADMIN +" WHERE email = '" + email +
                             "' and hashedPass = '" + hashedPass + "';";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        return cursor.moveToFirst();
    }


    public float getAveRateTeacher(int teacherId) {

        float ave = (float) 2.5;

        // Select All Query
        String selectQuery = "SELECT AVG(rating) FROM " + TABLE_COMMENT +
                             " WHERE " + KEY_COMMENT_TEACHERID +
                             " = '" + teacherId + "';";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
                ave = cursor.getFloat(0);
        }

        return ave;
    }

    public int getNReviewsTeacher(int teacherId) {

        int nReviews = 0;

        // Select All Query
        String selectQuery = "SELECT COUNT(*) FROM " + TABLE_COMMENT +
                             " WHERE " + KEY_COMMENT_TEACHERID +
                             " = '" + teacherId + "';";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            nReviews = cursor.getInt(0);
        }

        return nReviews;
    }

   /* public Comment getComment(Student student, Teacher teacher) {

        Comment comment    = null;
        String selectQuery = "SELECT  * FROM " + TABLE_COMMENT +" WHERE " +
                KEY_COMMENT_REVIEWER + " = '" + student.getEmail() +
                "' and " + KEY_COMMENT_TEACHERID + " = '" +
                teacher.getTeacherId() + "';";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            comment = new Comment();
            comment.setId(cursor.getInt(0));
            comment.setTitle(cursor.getString(1));
            comment.setBody(cursor.getString(2));
            comment.setRate(cursor.getFloat(3));
            comment.setReviewer(cursor.getString(4));
            *//*comment.setTeacherID(cursor.getInt(5));*//*
        }

        return comment;
    }*/

/*    public Student getStudent(String reviewer) {

        Student student = null;

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_STUDENT +
                             " WHERE " + KEY_EMAIL +
                             " = '" + reviewer + "';";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {

                student = new Student();
                student.setStudentId(cursor.getInt(0));
                student.setEmail(cursor.getString(1));
                student.setHashedPass(cursor.getString(2));
        }

        return student;
    }*/

/*    public List<Comment> getAllCommentsPerTeacher(int teacherId) {

        List<Comment> commentList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_COMMENT +
                             " WHERE teacherId = '" + teacherId + "';";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                Comment comment = new Comment();
                System.out.println("Comment = {id: " + cursor.getInt(0) + " || title: "+ cursor.getString(1) + "}");
                comment.setId(cursor.getInt(0));
                comment.setTitle(cursor.getString(1));
                comment.setBody(cursor.getString(2));
                comment.setRate(cursor.getFloat(3));
                comment.setReviewer(cursor.getString(4));
*//*                comment.setTeacherID(cursor.getInt(5));*//*

                commentList.add(comment);

            } while (cursor.moveToNext());
        }

        return commentList;
    }*/


    /*public List<Teacher> getTeacher(String teacherName) {

        List<Teacher> teacherList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TEACHER +
                             " WHERE " + KEY_TEACHER_NAME + " LIKE '%" + teacherName + "%';";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                Teacher teacher = new Teacher();
                teacher.setTeacherId(Integer.parseInt(cursor.getString(0)));
                teacher.setName(cursor.getString(1));
                teacher.setDepartment(cursor.getString(2));
                teacher.setAveRating(getAveRateTeacher(teacher.getTeacherId()));
                teacher.setnReviews(getNReviewsTeacher(teacher.getTeacherId()));
                System.out.println(teacher.getName());
                teacherList.add(teacher);

            } while (cursor.moveToNext());
        }

        return teacherList;
    }*/

   /* public List<Teacher> getAllFollowedTeachers(Student student) {

        List<Teacher> teacherList = new ArrayList<Teacher>();
// id / teacherName/ teacherDepartment
        // Select All Query
        String selectQuery = "SELECT t." + KEY_ID + ", t." + KEY_TEACHER_NAME +
                                ", t." + KEY_TEACHER_DEPARTMENT +
                             " FROM " + TABLE_FOLLOWING_PROF + " AS fP, " +
                                TABLE_TEACHER + " AS t " +
                             "WHERE fp." + KEY_FOLLOWING_PROF_STUDENTID +
                                 " = " + student.getStudentId() + " AND fp." +
                                 KEY_FOLLOWING_PROF_TEACHERID + " = t." + KEY_ID;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                Teacher teacher = new Teacher();
                teacher.setTeacherId(Integer.parseInt(cursor.getString(0)));
                teacher.setName(cursor.getString(1));
                teacher.setDepartment(cursor.getString(2));
                teacher.setAveRating(getAveRateTeacher(teacher.getTeacherId()));
                teacher.setnReviews(getNReviewsTeacher(teacher.getTeacherId()));

                teacherList.add(teacher);

            } while (cursor.moveToNext());
        }

        return teacherList;
    }
*/
   /* public List<Teacher> getAllTeachers() {

        List<Teacher> teacherList = new ArrayList<Teacher>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TEACHER + " ORDER BY " + KEY_TEACHER_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                Teacher teacher = new Teacher();
                teacher.setTeacherId(Integer.parseInt(cursor.getString(0)));
                teacher.setName(cursor.getString(1));
                teacher.setDepartment(cursor.getString(2));
                teacher.setAveRating(getAveRateTeacher(teacher.getTeacherId()));
                teacher.setnReviews(getNReviewsTeacher(teacher.getTeacherId()));

                teacherList.add(teacher);

            } while (cursor.moveToNext());
        }

        return teacherList;
    }*/

   /* public List<Admin> getAllAdmin() {

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
    }*/

/*    public List<Student> getAllStudents() {

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
    }*/

    public List<Suggest> getAllSuggestions() {

        List<Suggest> suggestList = new ArrayList<Suggest>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SUGGEST_PROF;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                Suggest suggest = new Suggest();
                suggest.setName(cursor.getString(0));
                suggest.setDepartment(cursor.getString(1));
                suggest.setSuggestedBy(cursor.getString(2));


                suggestList.add(suggest);

            } while (cursor.moveToNext());
        }

        return suggestList;
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
