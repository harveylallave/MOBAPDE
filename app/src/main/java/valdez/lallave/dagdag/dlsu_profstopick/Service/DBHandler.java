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

public class DBHandler  {
    //FIREBASE DB LINK
    /*     https://mobapde-46925.firebaseio.com/    */
    //Firebase variables
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    final DatabaseReference studentDatabaseReference = databaseReference.child("student");
    final DatabaseReference teacherDatabaseReference = databaseReference.child("teacher");
    final DatabaseReference commentDatabaseReference = databaseReference.child("comment");
    final DatabaseReference adminDatabaseReference = databaseReference.child("admin");

    String key; //Key used to push in the database

    public DBHandler() {
    }

    public void addNewComment(Comment newComment) {
        key = studentDatabaseReference.push().getKey();

        commentDatabaseReference.child(key).setValue(newComment);
    }

    public void addNewTeacher(Teacher newTeacher) {
        key = teacherDatabaseReference.push().getKey();

        teacherDatabaseReference.child(key).setValue(newTeacher);

    }

    public void addNewAdmin(Admin newAdmin) {
        key = adminDatabaseReference.push().getKey();

        adminDatabaseReference.child(key).setValue(newAdmin);

    }

    public void addNewStudent(Student newStud) {
        key = studentDatabaseReference.push().getKey();

        studentDatabaseReference.child(key).setValue(newStud);


    }


}
