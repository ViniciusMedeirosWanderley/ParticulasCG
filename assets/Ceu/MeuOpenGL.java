package opengl;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2.*;
import com.jogamp.opengl.GL2;
import static com.jogamp.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW_MATRIX;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.math.VectorUtil;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import opengl.control.Camera;
import opengl.model.Face;
import opengl.model.LoadModelos;
import opengl.model.Modelo;
import opengl.part.Fogueira;
import opengl.part.LoadTextura;
import opengl.part.SistemaParticulas;

/**
 *
 * @author Leonardo Villeth
 */
public class MeuOpenGL implements GLEventListener{

    private GLU glu;
    public static GL2 gl2;
    
    SistemaParticulas sp;
    int[] textura_particula;
    int[] textura_grass;
    int[] textura_night;    
    
    Camera cam;
    
    Modelo arvore;
    Modelo arvore2; //Arvore feia
    Modelo arvore3; //Pinheiro
    Modelo fogueira;
    Modelo barraca;
    Modelo cenario;
    
    Texture textura_fogueira;
    Texture textura_arvore;
    Texture textura_arvore3;
    Texture textura_barraca;
    Texture textura_cenario;
    Texture textura_ceu;
    
    //Luz
    float[] diffuse  = new float[3];
    float[] specular = new float[3];
    float[] ambient  = new float[3];

    public MeuOpenGL(Camera c) {
        cam = c;
    }
    
    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();         // get the OpenGL graphics context                
        glu = GLU.createGLU(gl);                    // get GL Utilities        
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);    // set background (clear) color
        gl.glClearDepth(1.0f);                    // set clear depth value to farthest
        
        ///////////////////////////////////
        gl.glEnable(GL_DEPTH_TEST);               // enables depth testing
        gl.glDepthFunc(GL_LEQUAL);                     // the type of depth test to do        
        
        //gl.glEnable(GL_CULL_FACE);
        ///////////////////////////////////
        
        gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); // best perspective correction
        gl.glShadeModel(GL_SMOOTH);                     // blends colors nicely, and smoothes out lighting                     
        
        MeuOpenGL.gl2 = drawable.getGL().getGL2();        
        
        // Ativa o Alpha
        gl.glEnable(GL_BLEND); //Enable alpha blending
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA); //Set the blend function
        //////////////////////////////////
                
        System.out.println("Carregando Texturas...");
        
        File alpha = new File("img/circlealpha.bmp");
        File rgb = new File("img/circle.bmp");
        File grass = new File("img/grass1.bmp");
        File night = new File("img/night.bmp");
        File fog = new File("assets/Fogueira/text_fogueira.png");
        File txtArvore = new File("assets/Arvore/txtArvore.jpg");
        File txtArvore2 = new File("assets/Arvore/txtArvore2.jpg");
        File txtBarraca = new File("assets/Barraca/textBarraca.bmp");
        File txtCenario = new File("assets/Cenario/textCenario.bmp");
        File txtCeu5k = new File("assets/Ceu/ceu5k.jpg");
        
        try {            
            textura_particula = LoadTextura.loadTexturaAlpha(rgb, alpha);
            textura_grass = LoadTextura.loadTexturaGrass(grass);
            textura_night = LoadTextura.loadTexturaGrass(night);            
            
            textura_fogueira = TextureIO.newTexture(fog, false);
            textura_arvore = TextureIO.newTexture(txtArvore, false);
            textura_arvore3 = TextureIO.newTexture(txtArvore2, false);
            textura_barraca = TextureIO.newTexture(txtBarraca, false);
            textura_cenario = TextureIO.newTexture(txtCenario, false);
            textura_ceu = TextureIO.newTexture(txtCeu5k, false);
        } catch (IOException ex) {
            System.err.println("Erro carregando textura");
        }        
        
        //////////////////////////////////
        //Carrega Modelos
        System.out.println("Carregando Modelos...");
        try {            
            arvore = LoadModelos.loadObj(new File("assets/Tree/lowpolytree.obj"));           
            fogueira = LoadModelos.loadObj(new File("assets/Fogueira/fogueira.obj"));            
            arvore2 = LoadModelos.loadObj(new File("assets/Arvore/arvore.obj"));           
            arvore3 = LoadModelos.loadObj(new File("assets/Arvore/arvore2.obj"));
            barraca = LoadModelos.loadObj(new File("assets/Barraca/barraca.obj"));
            cenario = LoadModelos.loadObj(new File("assets/Cenario/Cenario_Floresta.obj"));
        } catch (IOException ex) {
            System.err.println("Erro carregando modelo.");
        }        
        //////////////////////////////////
        
        /// Luz        
        gl.glEnable(GL_LIGHT0);
        gl.glLightfv(GL_LIGHT0, GL_AMBIENT, new float[]{0.1f,0.1f,0.1f,1.0f}, 0);            
        
        
        float step = 1.0f/30.0f; //30fps
        //float step = 0.01f;        
        float tamanho = 0.09f;//0.08f;
        //int numeroParticulas = 100;
        //sp = new SistemaParticulas(textura_particula, step, tamanho);
        //sp.criarEmissorDefault(numeroParticulas);
        sp = new Fogueira(textura_particula, step, tamanho);
        ((Fogueira)sp).setPos(0.0f, -0.35f, 1.0f); //z= - 1
        /***********************/
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        
    }        
    
    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color and depth buffers        
        
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity();   
        
        glu.gluLookAt(cam.getPos()[0], cam.getPos()[1], cam.getPos()[2],
                      cam.getLook()[0], cam.getLook()[1], cam.getLook()[2],
                      cam.getUp()[0], cam.getUp()[1], cam.getUp()[2]);                
        
        

        //gl.glEnable(GL_CULL_FACE);        
        
        //gl.glFrontFace(GL_CCW);
        
        gl.glPushMatrix();
            gl.glDisable(GL_BLEND);
        //gl.glCullFace(GL_FRONT);                            
            desenha(gl);
        //gl.glCullFace(GL_BACK);                    
          //  desenha(gl);
          gl.glEnable(GL_BLEND);
        gl.glPopMatrix();
        
        
        gl.glDisable(GL_LIGHTING);   
        ///////////////////////////////////////
        //gl.glDisable(GL_CULL_FACE);                
        desenhaFogueiraSemRotacao(gl);                
        ////////////////////////////////////////
        
        drawable.swapBuffers();        
    }
    
    private void desenha(GL2 gl){        
        gl.glPushMatrix();
            gl.glDisable(GL_DEPTH_TEST);
            desenhaEsfera();
            gl.glEnable(GL_DEPTH_TEST);
        gl.glPopMatrix();
        
        gl.glEnable(GL_LIGHTING);
        
        /*gl.glPushMatrix();
            gl.glColor3f(1f,1f,1f);
            gl.glScalef(2f, 2f, 2f);
            gl.glTranslatef(0f,-0.5f,0f);
            gl.glEnable(GL_TEXTURE_2D);
            textura_arvore.bind(gl);
            desenhaModelo(gl,arvore2);
            gl.glDisable(GL_TEXTURE_2D);
        gl.glPopMatrix();*/
        
        /*gl.glPushMatrix();
            gl.glColor3f(1f,1f,1f);
            gl.glScalef(1f, 1f, 1f);
            gl.glTranslatef(-0.5f,-0.5f,5.0f);
            gl.glEnable(GL_TEXTURE_2D);
            textura_arvore3.bind(gl);
            desenhaModelo(gl,arvore3);
            gl.glDisable(GL_TEXTURE_2D);
        gl.glPopMatrix();*/
        
        /*gl.glPushMatrix();
            gl.glColor3f(1f,1f,1f);
            gl.glScalef(0.5f, 0.5f, 0.5f);
            gl.glRotatef(90, 0f, 1.0f, 0f);
            gl.glTranslatef(-5.0f,-1.0f,5.0f);
            gl.glEnable(GL_TEXTURE_2D);
            textura_barraca.bind(gl);
            desenhaModelo(gl,barraca);
            gl.glDisable(GL_TEXTURE_2D);
        gl.glPopMatrix();*/
        
        gl.glPushMatrix();
            gl.glColor3f(1f,1f,1f);
            gl.glScalef(0.5f, 0.5f, 0.5f);
            gl.glTranslatef(-0.5f, -1.32f, 4.40f);
            gl.glEnable(GL_TEXTURE_2D);
            textura_cenario.bind(gl);
            desenhaModelo(gl,cenario);
            gl.glDisable(GL_TEXTURE_2D);
        gl.glPopMatrix();
        
        /*// Desenha Arvores
        gl.glPushMatrix();                               
            gl.glColor3f(1f,0f,0f);
            /// Luz
            gl.glEnable(GL_LIGHTING);   
            gl.glEnable(GL_LIGHT0);
            gl.glLightfv(GL_LIGHT0, GL_AMBIENT, new float[]{0.3f,0.3f,0.3f}, 0);
            //gl.glLightfv(GL_LIGHT0, GL_SPECULAR, new float[]{1f,1f,1f}, 0);            
            float[] diffuse = {0.029850f,0.106291f,0.017666f,1};
            float[] specular = {0.085514f,0.355277f,0.074845f,1};
            gl.glMaterialfv(GL_FRONT, GL_AMBIENT, new float[]{0f,0f,0f,1}, 0);            
            gl.glMaterialfv(GL_FRONT, GL_DIFFUSE,  diffuse, 0);            
            gl.glMaterialfv(GL_FRONT, GL_SPECULAR, specular, 0);            
            
            ///
            gl.glTranslatef(-8.5f, 1.0f, 0);
            for (int i = 0; i < 10; i++) {                
                //desenhaModelo(gl, arvore);
                gl.glTranslatef(2.0f, 0, 0);
                gl.glMaterialfv(GL_FRONT, GL_DIFFUSE,  diffuse, 0);            
                gl.glMaterialfv(GL_FRONT, GL_SPECULAR, specular, 0);            
            }
            
            
            gl.glDisable(GL_LIGHTING);
        gl.glPopMatrix();
        */
        // Desenha Fogueira
        gl.glPushMatrix();                               
            gl.glColor3f(1f,1f,1f);
            gl.glTranslatef(-0.04f, -0.49f, 1.95f);
            gl.glScalef(0.12f, 0.12f, 0.12f);//0.16f            
            
            diffuse[0] = diffuse[1] = diffuse[2] = 0.64f;            
            specular[0] = specular[1] = specular[2] = 0.029703f;
            
            gl.glMaterialfv(GL_FRONT, GL_AMBIENT, VectorUtil.VEC3_ONE, 0);            
            gl.glMaterialfv(GL_FRONT, GL_DIFFUSE,  diffuse, 0);            
            gl.glMaterialfv(GL_FRONT, GL_SPECULAR, specular, 0);            
            
            gl.glEnable(GL_TEXTURE_2D);
            textura_fogueira.bind(gl);            
            desenhaModelo(gl,fogueira);
            gl.glDisable(GL_TEXTURE_2D);
            
        gl.glPopMatrix();                        
                   
        // desenha chao
        gl.glPushMatrix();            
            desenhaChao(gl);            
        gl.glPopMatrix();        
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context              
        
        if (height == 0) // prevent divide by zero
            height = 1;   
        float aspect = (float)width / height;

        // Set the view port (display area) to cover the entire window
        gl.glViewport(0, 0, width, height);        

        // Setup perspective projection, with aspect ratio matches viewport
        gl.glMatrixMode(GL_PROJECTION);  // choose projection matrix
        gl.glLoadIdentity();             // reset projection matrix
        glu.gluPerspective(45.0, aspect, 1.0, 100.0); // fovy, aspect, zNear, zFar

        // Enable the model-view transform
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity(); // reset  
    }    
    
    private void desenhaFogueiraSemRotacao(GL2 gl){
        float[] modelview = new float[16];
        int i,j;        
        float fx = ((Fogueira)sp).getX();        
        float fz = ((Fogueira)sp).getZ();                                
        // save the current modelview matrix
        gl.glPushMatrix();            
            gl.glTranslatef(fx, 0, fz+fz);        
            // get the current modelview matrix                
            gl.glGetFloatv(GL_MODELVIEW_MATRIX , FloatBuffer.wrap(modelview));
            /// undo all rotations
            // beware all scaling is lost as well 
            for( i=0; i<3; i++ ) 
                for( j=0; j<3; j++ ) {
                    if ( i==j )
                        modelview[i*4+j] = 1.0f;
                    else
                        modelview[i*4+j] = 0.0f;
            }                       
            
            /// set the modelview with no rotations and scaling
            gl.glLoadMatrixf(FloatBuffer.wrap(modelview));
            
            gl.glTranslatef(-fx, 0, -fz-fz);
            
            sp.draw();
            sp.step();            
            
            /// restores the modelview matrix
            gl.glPopMatrix();        
    }
    
    private void desenhaEsfera(){        
        GL2 gl = gl2;
        
        //gl.glColor4f(0f, 0f, 0.111f, 1f);
        gl.glColor4f(0.5f, 0.5f, 0.5f, 1.0f);
        gl.glTranslatef(cam.getPos()[0], -0.5f, cam.getPos()[2]);
        
        GLUquadric esfera = glu.gluNewQuadric();
        textura_ceu.enable(gl);
        textura_ceu.bind(gl);
        glu.gluQuadricTexture(esfera, true);
        glu.gluSphere(esfera, 10, 20, 20);
        textura_ceu.disable(gl);
        //glu.gluSphere(glu.gluNewQuadric(), 10,20,20);                    
    }
    
    private void desenhaModelo(GL2 gl, Modelo m){               
        float x,y,z;
        float nx,ny,nz;
        float tx,ty;
        int vIndex, nIndex, tIndex;
        float[] vertice;
        float[] normal;
        float[] textura;        
        
        for (Face face : m.getFaces()) {             
            gl.glBegin(GL_TRIANGLE_FAN); //GL_TRIANGLE_FAN // GL_TRIANGLES  // GL_TRIANGLE_STRIP // GL_QUADS                
                for (int i = 0; i < face.getVertices().size(); i++) {
                    vIndex = face.getVertices().get(i) - 1;

                    vertice = m.getVertices().get(vIndex);
                    x = vertice[0];
                    y = vertice[1];
                    z = vertice[2];                
                    
                    nIndex = face.getVNormais().get(i) - 1;
                    normal = m.getVNormais().get(nIndex);
                    nx = normal[0];
                    ny = normal[1];
                    nz = normal[2];                    
                    
                    if((m == arvore) && (nIndex > 110)){
                        gl.glMaterialfv(GL_FRONT, GL_DIFFUSE, new float[]{0.176206f,0.051816f,0.016055f,1}, 0);            
                        gl.glMaterialfv(GL_FRONT, GL_SPECULAR, new float[]{0.015532f,0.005717f,0.002170f,1}, 0);            
                    }
                    
                    //Coordenadas da Textura (se houver)
                    if(face.getVTexturas().size() > 0){
                        tIndex = face.getVTexturas().get(i) - 1;
                        textura = m.getVTexts().get(tIndex);
                        tx = textura[0];
                        ty = textura[1];                        
                        
                        gl.glTexCoord2f(tx, ty);
                    }
                    
                    //Normal do triangulo
                    gl.glNormal3f(nx, ny, nz);
                    //Vertices do triangulo
                    gl.glVertex3f(x, y, z);                     
                }
            gl.glEnd();                          
        }    
    }
    
    private void desenhaTrianguloTeste(){        
        GL2 gl = gl2;                     
        //Desenha um triangulo na tela
        gl.glBegin(GL_TRIANGLES);             
            //Vertices do triangulo
            gl.glVertex3f(0.0f -0.5f, 0.0f, -5.0f); 
            gl.glVertex3f(0.5f -0.5f, 1.0f, -5.0f); 
            gl.glVertex3f(1.0f -0.5f, 0.0f, -5.0f );                        
        gl.glEnd();              
    }
    
    private void desenhaUmaParticulaTeste(){
        GL2 gl = gl2;
        // Desenha uma particula na tela com textura Alpha
        gl.glEnable(GL_TEXTURE_2D);
        gl.glBindTexture(GL_TEXTURE_2D, textura_particula[0]);
        float z = -5.0f;
        gl.glColor4f(1f,1f,1f,1f);
        gl.glBegin(GL_TRIANGLE_STRIP); // Build Quad From A Triangle Strip
                gl.glTexCoord2d(1, 1);
                gl.glVertex3f(0.5f, 0.5f, z); // Top Right                
                
                gl.glTexCoord2d(0, 1);                
                gl.glVertex3f(0f, 0.5f, z); // Top Left                
                
                gl.glTexCoord2d(1, 0);
                gl.glVertex3f(0.5f, 0f, z); // Bottom Right                
                
                gl.glTexCoord2d(0, 0);
                gl.glVertex3f(0f, 0f, z); // Bottom Left                
        gl.glEnd();        
        gl.glDisable(GL_TEXTURE_2D);            
    }

    private void desenhaChao(GL2 gl) {
        gl.glColor4f(0f,1f,0f,0.5f);
        gl.glEnable(GL_TEXTURE_2D);
        gl.glBindTexture(GL_TEXTURE_2D, textura_grass[0]);        
        gl.glBegin(GL_TRIANGLE_STRIP); // Build Quad From A Triangle Strip 
                gl.glTexCoord2d(100, 100);
                gl.glVertex3f(100.0f, -0.5f, -100.0f); // Top Right                
                
                gl.glTexCoord2d(0, 100);
                gl.glVertex3f(-100.0f, -0.5f, -100.0f); // Top Left                
                
                gl.glTexCoord2d(100, 0);
                gl.glVertex3f(100.0f, -0.5f, 100.0f); // Bottom Right                
                
                gl.glTexCoord2d(0, 0);
                gl.glVertex3f(-100.0f, -0.5f, 100.0f); // Bottom Left
        gl.glEnd();
        gl.glDisable(GL_TEXTURE_2D);        
    }    
}
