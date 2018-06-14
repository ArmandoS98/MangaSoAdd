package armando.gt.com.aplicaionllenar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;

import armando.gt.com.aplicaionllenar.Modals.Home;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements  GoogleApiClient.OnConnectionFailedListener{
    private static final String TAG = "MainActivity";
    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText mEditTextTitulo;
    private EditText mEditTextDescripcion;
    private EditText mEditTextRanking;
    private EditText mEditTextTemprada;
    private ImageButton mButtonPortada;
    private CircleImageView profile;

    private View mParentLayout;
    private Bitmap bitmap;


    //Firebase
    //firebase
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mParentLayout = findViewById(android.R.id.content);
        mEditTextTitulo = findViewById(R.id.et_titulo);
        mEditTextDescripcion = findViewById(R.id.et_descripcion);
        mEditTextRanking = findViewById(R.id.et_ranking);
        mEditTextTemprada = findViewById(R.id.et_temporadas);
        mButtonPortada = findViewById(R.id.btn_portada);


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,  this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        //Metodo de verificacion de las diferentes cuentas de acceso a la app
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            mFirebaseUser = null;

            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        if (mFirebaseUser != null){

            //TODO: ESTO PERMITE CAMBIARLE EL TEXTO EN EL ENCABEZADO

            //final TextView nav_user = findViewById(R.id.nombre);
             profile = findViewById(R.id.img_user);

            //nav_user.setText(mFirebaseUser.getDisplayName());

            if (mFirebaseUser.getPhotoUrl() != null) {
                Glide.with(this).load(mFirebaseUser.getPhotoUrl()).into(profile);
            }
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewAnime(mEditTextTitulo.getText().toString(), mEditTextDescripcion.getText().toString(), mEditTextRanking.getText().toString(), mEditTextTemprada.getText().toString());
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

        mButtonPortada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Imagen"), PICK_IMAGE_REQUEST);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            mFirebaseAuth.signOut();
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);

            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void createNewAnime(String titulo, String descripcion, String ranking, String temporadas) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference newNoteRef = db.collection("Cartelera").document();


        Home note = new Home();
        note.setTitulo(titulo);
        note.setDescripcion(descripcion);
        note.setRanking(ranking);
        note.setTemporadas(temporadas);
        note.setCartelera_id(newNoteRef.getId());
        note.setId_pos("1");

        newNoteRef.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    makeSnackBarMessage("Created a new note");
                    mEditTextTitulo.setText("");
                    mEditTextDescripcion.setText("");
                    mEditTextRanking.setText("");
                    mEditTextTemprada.setText("");
                }else{
                    makeSnackBarMessage("Failed. Check Log");
                }
            }
        });
    }

    private void makeSnackBarMessage(String message){
        Snackbar.make(mParentLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Cómo obtener el mapa de bits de la Galería
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Configuración del mapa de bits en ImageView
                Glide.with(this).load(bitmap).into(mButtonPortada);
                //imagen.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
