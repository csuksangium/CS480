/**
 * PA2.java - driver for the Insect model simulation
 * 
 * 
 * 18th October 2016
 * 
 * - added documentation
 * 
 * Chanan Suksangium
 * 
 */


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.awt.GLCanvas;//for new version of gl
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

import com.jogamp.opengl.util.FPSAnimator;//for new version of gl
import com.jogamp.opengl.util.gl2.GLUT;//for new version of gl

/**
 * The main class which drives the hand model simulation.
 * 
 * @author Chanan Suksangium
 * csuksan@bu.edu
 * @date October 18 2016
 */
public class PA2 extends JFrame implements GLEventListener, KeyListener,
    MouseListener, MouseMotionListener {

  /**
   * A Leg which has a palm joint, a middle joint, and a distal joint.
   * 
   * @author Chanan Suksangium csuksan@bu.edu
   * @since October 2016
   */
  private class Leg {
    /** The distal joint of this Leg. */
    private final Component legLower;
    /** The list of all the joints in this Leg. */
    private final List<Component> joints;
    /** The middle joint of this Leg. */
    private final Component middleJoint;
    /** The palm joint of this Leg. */
    private final Component legUpper;
    
    
    /**
     * Instantiates this Leg with the three specified joints.
     * 
     * @param legUpper
     *          The upper joint of this Leg.
     * @param middleJoint
     *          The middle joint of this Leg.
     * @param legLower
     *          The lower joint of this Leg.
     */
    public Leg(final Component legUpper, final Component middleJoint,
        final Component legLower) {
      this.legUpper = legUpper;
      this.middleJoint = middleJoint;
      this.legLower = legLower;
      this.joints = Collections.unmodifiableList(Arrays.asList(this.legUpper,
          this.middleJoint, this.legLower));
    }

    /**
     * Gets the lower joint of this Leg.
     * 
     * @return The lower joint of this Leg.
     */
    Component legLower() {
      return this.legLower;
    }

    /**
     * Gets an unmodifiable view of the list of the joints of this Leg.
     * 
     * @return An unmodifiable view of the list of the joints of this Leg.
     */
    List<Component> joints() {
      return this.joints;
    }

    /**
     * Gets the middle joint of this Leg.
     * 
     * @return The middle joint of this Leg.
     */
    Component middleJoint() {
      return this.middleJoint;
    }

    /**
     * Gets the upper joint of this Leg.
     * 
     * @return The upper joint of this Leg.
     */
    Component legUpper() {
      return this.legUpper;
    }
  }

  /** The color for components which are selected for rotation. */
  public static final FloatColor ACTIVE_COLOR = FloatColor.RED;
  /** The default width of the created window. */
  public static final int DEFAULT_WINDOW_HEIGHT = 512;
  /** The default height of the created window. */
  public static final int DEFAULT_WINDOW_WIDTH = 512;
  /** The height of the lower joint on each of the Legs. */
  public static final double LOWER_JOINT_HEIGHT = 0.5;
  /** The radius of each joint which comprises the Leg. */
  public static final double Leg_RADIUS = 0.09;
  //Radius of the body of insect before stretching
  public static final double BODY_RADIUS = 0.5;
  /** The color for components which are not selected for rotation. */
  public static final FloatColor INACTIVE_COLOR = FloatColor.BLUE;
  /** The initial position of the top level component in the scene. */
  public static final Point3D INITIAL_POSITION = new Point3D(0.5, 0, 3);
  /** The height of the middle joint on each of the Legs. */
  public static final double MIDDLE_JOINT_HEIGHT = 0.04;
  /** The height of the palm joint on each of the Legs. */
  public static final double UPPER_JOINT_HEIGHT = 0.4;
  /** The angle by which to rotate the joint on user request to rotate. */
  public static final double ROTATION_ANGLE = 2.0;
  /** Randomly generated serial version UID. */
  private static final long serialVersionUID = -7060944143920496524L;

  /**
   * Runs the bug simulation in a single JFrame.
   * 
   * @param args
   *          This parameter is ignored.
   */
  public static void main(final String[] args) {
    new PA2().animator.start();
  }

  /**
   * The animator which controls the framerate at which the canvas is animated.
   */
  final FPSAnimator animator;
  /** The canvas on which we draw the scene. */
  private final GLCanvas canvas;
  /** The capabilities of the canvas. */
  private final GLCapabilities capabilities = new GLCapabilities(null);
  /** The Legs on the hand to be modeled. */
  private final Leg[] Legs;
  /** The forearm to be modeled. */
  //private final Component forearm;
  /** The OpenGL utility object. */
  private final GLU glu = new GLU();
  /** The OpenGL utility toolkit object. */
  private final GLUT glut = new GLUT();
  /** The hand to be modeled. */
  private final Component body;
  /** The last x and y coordinates of the mouse press. */
  private int last_x = 0, last_y = 0;
  /** Whether the world is being rotated. */
  private boolean rotate_world = false;
  /** The axis around which to rotate the selected joints. */
  private Axis selectedAxis = Axis.X;
  /** The set of components which are currently selected for rotation. */
  private final Set<Component> selectedComponents = new HashSet<Component>(18);
  /**
   * The set of Legs which have been selected for rotation.
   * 
   * Selecting a joint will only affect the joints in this set of selected
   * Legs.
   **/
  private final Set<Leg> selectedLegs = new HashSet<Leg>(5);
  /** Whether the state of the model has been changed. */
  private boolean stateChanged = true;
  /**
   * The top level component in the scene which controls the positioning and
   * rotation of everything in the scene.
   */
  private final Component topLevelComponent;

  /** The quaternion which controls the rotation of the world. */
  private Quaternion viewing_quaternion = new Quaternion();
  /** The set of all components. */
  private final List<Component> components;

  public static String BACK_RIGHT_UPPER = "back right upper leg";
  public static String BACK_RIGHT_MIDDLE = "back right middle leg joint";
  public static String BACK_RIGHT_LOWER = "back right lower leg";
  public static String MIDDLE_LEFT_UPPER = "middle left upper leg";
  public static String MIDDLE_LEFT_MIDDLE = "middle left middle leg joint";
  public static String MIDDLE_LEFT_LOWER = "middle left lower leg";
  public static String FRONT_LEFT_UPPER = "front left upper leg";
  public static String FRONT_LEFT_MIDDLE = "front left middle leg joint";
  public static String FRONT_LEFT_LOWER = "front left lower leg";
  public static String BACK_LEFT_UPPER = "back left upper leg";
  public static String BACK_LEFT_MIDDLE = "back left middle leg joint";
  public static String BACK_LEFT_LOWER = "back left lower leg";
  public static String MIDDLE_RIGHT_UPPER = "middle right upper leg";
  public static String MIDDLE_RIGHT_MIDDLE = "middle right middle leg joint";
  public static String MIDDLE_RIGHT_LOWER = "middle right lower leg";
  public static String FRONT_RIGHT_UPPER = "front right upper leg";
  public static String FRONT_RIGHT_MIDDLE = "front right middle leg";
  public static String FRONT_RIGHT_LOWER = "front right lower leg";
  public static String BODY_NAME = "body";
  public static String TOP_LEVEL_NAME = "top level";

  /**
   * Initializes the necessary OpenGL objects and adds a canvas to this JFrame.
   */
  public PA2() {
    this.capabilities.setDoubleBuffered(true);

    this.canvas = new GLCanvas(this.capabilities);
    this.canvas.addGLEventListener(this);
    this.canvas.addMouseListener(this);
    this.canvas.addMouseMotionListener(this);
    this.canvas.addKeyListener(this);
    // this is true by default, but we just add this line to be explicit
    this.canvas.setAutoSwapBufferMode(true);
    this.getContentPane().add(this.canvas);

    // refresh the scene at 60 frames per second
    this.animator = new FPSAnimator(this.canvas, 60);

    this.setTitle("CS480/CS680 : Insect Simulator");
    this.setSize(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setVisible(true);

    // all the Lower legs
    final Component legLower1 = new Component(new Point3D(0, 0,
        MIDDLE_JOINT_HEIGHT), new RoundedCylinder(Leg_RADIUS,
        LOWER_JOINT_HEIGHT, this.glut), BACK_LEFT_LOWER);
    final Component legLower2 = new Component(new Point3D(0, 0,
        MIDDLE_JOINT_HEIGHT), new RoundedCylinder(Leg_RADIUS,
        LOWER_JOINT_HEIGHT, this.glut), MIDDLE_LEFT_LOWER);
    final Component legLower3 = new Component(new Point3D(0, 0,
        MIDDLE_JOINT_HEIGHT), new RoundedCylinder(Leg_RADIUS,
        LOWER_JOINT_HEIGHT, this.glut), FRONT_LEFT_LOWER);
    final Component legLower4 = new Component(new Point3D(0, 0,
        MIDDLE_JOINT_HEIGHT), new RoundedCylinder(Leg_RADIUS,
        LOWER_JOINT_HEIGHT, this.glut), BACK_RIGHT_LOWER);
    final Component legLower5 = new Component(new Point3D(0, 0,
        MIDDLE_JOINT_HEIGHT), new RoundedCylinder(Leg_RADIUS,
        LOWER_JOINT_HEIGHT, this.glut), MIDDLE_RIGHT_LOWER);
    final Component legLower6 = new Component(new Point3D(0, 0,
            MIDDLE_JOINT_HEIGHT), new RoundedCylinder(Leg_RADIUS,
            LOWER_JOINT_HEIGHT, this.glut), FRONT_RIGHT_LOWER);

    // all the middle joints
    final Component middle1 = new Component(new Point3D(0, 0,
        UPPER_JOINT_HEIGHT), new RoundedCylinder(Leg_RADIUS,
        MIDDLE_JOINT_HEIGHT, this.glut), BACK_LEFT_MIDDLE);
    final Component middle2 = new Component(new Point3D(0, 0,
        UPPER_JOINT_HEIGHT), new RoundedCylinder(Leg_RADIUS,
        MIDDLE_JOINT_HEIGHT, this.glut), MIDDLE_LEFT_MIDDLE);
    final Component middle3 = new Component(new Point3D(0, 0,
        UPPER_JOINT_HEIGHT), new RoundedCylinder(Leg_RADIUS,
        MIDDLE_JOINT_HEIGHT, this.glut), FRONT_LEFT_MIDDLE);
    final Component middle4 = new Component(new Point3D(0, 0,
        UPPER_JOINT_HEIGHT), new RoundedCylinder(Leg_RADIUS,
        MIDDLE_JOINT_HEIGHT, this.glut), BACK_RIGHT_MIDDLE);
    final Component middle5 = new Component(new Point3D(0, 0,
        UPPER_JOINT_HEIGHT), new RoundedCylinder(Leg_RADIUS,
        MIDDLE_JOINT_HEIGHT, this.glut), MIDDLE_RIGHT_MIDDLE);
    final Component middle6 = new Component(new Point3D(0, 0,
            UPPER_JOINT_HEIGHT), new RoundedCylinder(Leg_RADIUS,
            MIDDLE_JOINT_HEIGHT, this.glut), FRONT_RIGHT_MIDDLE);

    // Upper legs
    final Component legUpper1 = new Component(new Point3D(-0.6, 0, 0.8),
        new RoundedCylinder(Leg_RADIUS, UPPER_JOINT_HEIGHT, this.glut),
        BACK_LEFT_UPPER);
    final Component legUpper2 = new Component(new Point3D(0, 0, 0.8),
        new RoundedCylinder(Leg_RADIUS, UPPER_JOINT_HEIGHT, this.glut),
        MIDDLE_LEFT_UPPER);
    final Component legUpper3 = new Component(new Point3D(0.6, 0, 0.8),
        new RoundedCylinder(Leg_RADIUS, UPPER_JOINT_HEIGHT, this.glut),
        FRONT_LEFT_UPPER);
 
    final Component legUpper4 = new Component(new Point3D(0, 0, 0.2),
        new RoundedCylinder(Leg_RADIUS, UPPER_JOINT_HEIGHT, this.glut),
        BACK_RIGHT_UPPER);
    final Component legUpper5 = new Component(new Point3D(-0.6, 0, 0.2),
        new RoundedCylinder(Leg_RADIUS, UPPER_JOINT_HEIGHT, this.glut),
        MIDDLE_RIGHT_UPPER);
    final Component legUpper6 = new Component(new Point3D(0.6, 0, 0.2),
            new RoundedCylinder(Leg_RADIUS, UPPER_JOINT_HEIGHT, this.glut),
            FRONT_RIGHT_UPPER);
    

    // put together the Legs for easier selection by keyboard input later on
    this.Legs = new Leg[] { new Leg(legUpper1, middle1, legLower1),
        new Leg(legUpper2, middle2, legLower2),
        new Leg(legUpper3, middle3, legLower3),
        new Leg(legUpper4, middle4, legLower4),
        new Leg(legUpper5, middle5, legLower5),
        new Leg(legUpper6, middle6, legLower6),
        };

    // the body
    this.body = new Component(new Point3D(0, 0, 0), new Body(
        BODY_RADIUS, this.glut), BODY_NAME);


    // the top level component which provides an initial position and rotation
    // to the scene (but does not cause anything to be drawn)
    this.topLevelComponent = new Component(INITIAL_POSITION, TOP_LEVEL_NAME);

    //Adding body first to top level component so other legs can be the children
    this.topLevelComponent.addChild(this.body);
 
    //Upper connected to body
    //Middle joint connected to upper
    //lower connected to middle
    this.body.addChildren(legUpper1, legUpper2, legUpper3, legUpper4, legUpper5, legUpper6);
    legUpper1.addChild(middle1);
    legUpper2.addChild(middle2);
    legUpper3.addChild(middle3);
    legUpper4.addChild(middle4);
    legUpper5.addChild(middle5);
    legUpper6.addChild(middle6);
    middle1.addChild(legLower1);
    middle2.addChild(legLower2);
    middle3.addChild(legLower3);
    middle4.addChild(legLower4);
    middle5.addChild(legLower5);
    middle6.addChild(legLower6);

    //Position body to display from frontal view
    this.topLevelComponent.rotate(Axis.Y, 270);
    this.topLevelComponent.rotate(Axis.X, 40);

    //uniquely position individual legs
    legUpper1.rotate(Axis.X, 15);
    legLower1.rotate(Axis.X, 50);
    legUpper1.rotate(Axis.Y, -30);
    legUpper2.rotate(Axis.X, 15);
    legLower2.rotate(Axis.X, 50);
    legUpper3.rotate(Axis.X, 15);
    legLower3.rotate(Axis.X, 50);
    legUpper3.rotate(Axis.Y, 30);
    
    // rotate to make legs fit on opposite side
    legUpper4.rotate(Axis.Y, 180);
    legUpper4.rotate(Axis.X, -15);
    legLower4.rotate(Axis.X, 50);
    legUpper5.rotate(Axis.Y, 210);
    legUpper5.rotate(Axis.X, -15);
    legLower5.rotate(Axis.X, 50);
    legUpper6.rotate(Axis.Y, 150);
    legUpper6.rotate(Axis.X, -15);
    legLower6.rotate(Axis.X, 50);

    // set rotation limits for the upper joints of the Legs
    for (final Component legUpper : Arrays.asList(legUpper1, legUpper2, legUpper3, legUpper4, legUpper5, legUpper6)) {
      legUpper.setXPositiveExtent(20);
      legUpper.setXNegativeExtent(-20);
      legUpper.setZPositiveExtent(0);
      legUpper.setZNegativeExtent(0);
    }
    
    //Individually set y pos/neg extent due to different starting leg orientations
    legUpper1.setYPositiveExtent(-20);
    legUpper1.setYNegativeExtent(-50);
    legUpper2.setYPositiveExtent(15);
    legUpper2.setYNegativeExtent(-15);
    legUpper3.setYPositiveExtent(40);
    legUpper3.setYNegativeExtent(10);
    
    //4 middle right
    legUpper4.setYPositiveExtent(200);
    legUpper4.setYNegativeExtent(170);
    
    //5 back right
    legUpper5.setYPositiveExtent(220);
    legUpper5.setYNegativeExtent(190);
    //6 front right
    legUpper6.setYPositiveExtent(170);
    legUpper6.setYNegativeExtent(140);
    
    // set rotation limits for the middle joints of the Leg
    for (final Component middleJoint : Arrays.asList(middle1, middle2,
        middle3, middle4, middle5, middle6)) {
      middleJoint.setXPositiveExtent(45);
      middleJoint.setXNegativeExtent(-10);
      middleJoint.setYPositiveExtent(0);
      middleJoint.setYNegativeExtent(0);
      middleJoint.setZPositiveExtent(20);
      middleJoint.setZNegativeExtent(-20);
    }

    // set rotation limits for the lower joints of the Leg
    for (final Component legLower : Arrays.asList(legLower1, legLower2,
        legLower3, legLower4, legLower5, legLower6)) {
      legLower.setXPositiveExtent(60);
      legLower.setXNegativeExtent(30);
      legLower.setYPositiveExtent(0);
      legLower.setYNegativeExtent(0);
      legLower.setZPositiveExtent(0);
      legLower.setZNegativeExtent(0);
    }

    // create the list of all the components for debugging purposes
    this.components = Arrays.asList(legUpper1, middle1, legLower1, legUpper2, middle2,
        legLower2, legUpper3, middle3, legLower3, legUpper4, middle4, legLower4, legUpper5,
        middle5, legLower5, legUpper6, middle6, legLower6, this.body);
  }

  /**
   * Redisplays the scene containing the hand model.
   * 
   * @param drawable
   *          The OpenGL drawable object with which to create OpenGL models.
   */
  public void display(final GLAutoDrawable drawable) {
    final GL2 gl = (GL2)drawable.getGL();

    // clear the display
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

    // from here on affect the model view
    gl.glMatrixMode(GL2.GL_MODELVIEW);

    // start with the identity matrix initially
    gl.glLoadIdentity();

    // rotate the world by the appropriate rotation quaternion
    gl.glMultMatrixf(this.viewing_quaternion.toMatrix(), 0);

    // update the position of the components which need to be updated
    // TODO only need to update the selected and JUST deselected components
    if (this.stateChanged) {
      this.topLevelComponent.update(gl);
      this.stateChanged = false;
    }

    // redraw the components
    this.topLevelComponent.draw(gl);
  }

  /**
   * This method is intentionally unimplemented.
   * 
   * @param drawable
   *          This parameter is ignored.
   * @param modeChanged
   *          This parameter is ignored.
   * @param deviceChanged
   *          This parameter is ignored.
   */
  public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
      boolean deviceChanged) {
    // intentionally unimplemented
  }

  /**
   * Initializes the scene and model.
   * 
   * @param drawable
   *          {@inheritDoc}
   */
  public void init(final GLAutoDrawable drawable) {
    final GL2 gl = (GL2)drawable.getGL();

    // perform any initialization needed by the hand model
    this.topLevelComponent.initialize(gl);

    // initially draw the scene
    this.topLevelComponent.update(gl);

    // set up for shaded display of the hand
    final float light0_position[] = { 1, 1, 1, 0 };
    final float light0_ambient_color[] = { 0.25f, 0.25f, 0.25f, 1 };
    final float light0_diffuse_color[] = { 1, 1, 1, 1 };

    gl.glPolygonMode(GL.GL_FRONT, GL2.GL_FILL);
    gl.glEnable(GL2.GL_COLOR_MATERIAL);
    gl.glColorMaterial(GL.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE);

    gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    gl.glShadeModel(GL2.GL_SMOOTH);

    // set up the light source
    gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, light0_position, 0);
    gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, light0_ambient_color, 0);
    gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, light0_diffuse_color, 0);

    // turn lighting and depth buffering on
    gl.glEnable(GL2.GL_LIGHTING);
    gl.glEnable(GL2.GL_LIGHT0);
    gl.glEnable(GL2.GL_DEPTH_TEST);
    gl.glEnable(GL2.GL_NORMALIZE);
  }

  /**
   * Interprets key presses according to the following scheme:
   * 
   * up-arrow, down-arrow: increase/decrease rotation angle
   * 
   * @param key
   *          The key press event object.
   */
  public void keyPressed(final KeyEvent key) {
    switch (key.getKeyCode()) {
    case KeyEvent.VK_KP_UP:
    case KeyEvent.VK_UP:
      for (final Component component : this.selectedComponents) {
        component.rotate(this.selectedAxis, ROTATION_ANGLE);
      }
      this.stateChanged = true;
      break;
    case KeyEvent.VK_KP_DOWN:
    case KeyEvent.VK_DOWN:
      for (final Component component : this.selectedComponents) {
        component.rotate(this.selectedAxis, -ROTATION_ANGLE);
      }
      this.stateChanged = true;
      break;
    default:
      break;
    }
  }

  /**
   * This method is intentionally unimplemented.
   * 
   * @param key
   *          This parameter is ignored.
   */
  public void keyReleased(final KeyEvent key) {
    // intentionally unimplemented
  }

  private final TestCases testCases = new TestCases();

  private void setModelState(final Map<String, Angled> state) {
    this.body.setAngles(state.get(BODY_NAME));
    this.Legs[0].legUpper().setAngles(state.get(BACK_LEFT_UPPER));
    this.Legs[0].middleJoint().setAngles(state.get(BACK_LEFT_MIDDLE));
    this.Legs[0].legLower().setAngles(state.get(BACK_LEFT_LOWER));
    this.Legs[1].legUpper().setAngles(state.get(MIDDLE_LEFT_UPPER));
    this.Legs[1].middleJoint().setAngles(state.get(MIDDLE_LEFT_MIDDLE));
    this.Legs[1].legLower().setAngles(state.get(MIDDLE_LEFT_LOWER));
    this.Legs[2].legUpper().setAngles(state.get(FRONT_LEFT_UPPER));
    this.Legs[2].middleJoint().setAngles(state.get(FRONT_LEFT_MIDDLE));
    this.Legs[2].legLower().setAngles(state.get(FRONT_LEFT_LOWER));
    this.Legs[4].legUpper().setAngles(state.get(BACK_RIGHT_UPPER));
    this.Legs[4].middleJoint().setAngles(state.get(BACK_RIGHT_MIDDLE));
    this.Legs[4].legLower().setAngles(state.get(BACK_RIGHT_LOWER));
    this.Legs[3].legUpper().setAngles(state.get(MIDDLE_RIGHT_UPPER));
    this.Legs[3].middleJoint().setAngles(state.get(MIDDLE_RIGHT_MIDDLE));
    this.Legs[3].legLower().setAngles(state.get(MIDDLE_RIGHT_LOWER));
    this.Legs[5].legUpper().setAngles(state.get(FRONT_RIGHT_UPPER));
    this.Legs[5].middleJoint().setAngles(state.get(FRONT_RIGHT_MIDDLE));
    this.Legs[5].legLower().setAngles(state.get(FRONT_RIGHT_LOWER));
    this.stateChanged = true;
  }

  /**
   * Interprets typed keys according to the following scheme:
   * 
   * 1 : toggle the first Leg active in rotation
   * 
   * 2 : toggle the second Leg active in rotation
   * 
   * 3 : toggle the third Leg active in rotation
   * 
   * 4 : toggle the fourth Leg active in rotation
   * 
   * 5 : toggle the fifth Leg active in rotation
   * 
   * 6 : toggle the sixth leg active in rotation
   * 
   * X : use the X axis rotation at the active joint(s)
   * 
   * Y : use the Y axis rotation at the active joint(s)
   * 
   * Z : use the Z axis rotation at the active joint(s)
   * 
   * C : resets the hand to the stop sign
   * 
   * U : select upper joint that connects Leg to body
   * 
   * M : select middle joint
   * 
   * L : select lower leg joint
   * 
   * R : resets the view to the initial rotation
   * 
   * P : prints the angles of the 6 Legs for debugging purposes
   * 
   * K: Test cases cycle through
   * 
   * Q, Esc : exits the program
   * 
   */
  public void keyTyped(final KeyEvent key) {
    switch (key.getKeyChar()) {
    case 'Q':
    case 'q':
    case KeyEvent.VK_ESCAPE:
      new Thread() {
        @Override
        public void run() {
          PA2.this.animator.stop();
        }
      }.start();
      System.exit(0);
      break;

    // print the angles of the components
    case 'K':
    case 'k':
      printJoints();
      break;

    // resets to the stop sign
    case 'C':
    case 'c':
      this.setModelState(this.testCases.stop());
      break;

    // set the state of the hand to the next test case
    case 'P':
    case 'p':
      this.setModelState(this.testCases.next());
      break;

    // set the viewing quaternion to 0 rotation
    case 'R':
    case 'r':
      this.viewing_quaternion.reset();
      break;

    // Toggle which Leg(s) are affected by the current rotation
    case '1':
      toggleSelection(this.Legs[0]);
      break;
    case '2':
      toggleSelection(this.Legs[1]);
      break;
    case '3':
      toggleSelection(this.Legs[2]);
      break;
    case '4':
      toggleSelection(this.Legs[4]);
      break;
    case '5':
      toggleSelection(this.Legs[3]);
      break;
    case '6':
      toggleSelection(this.Legs[5]);
      break;


    // toggle which joints are affected by the current rotation
    case 'L':
    case 'l':
      for (final Leg Leg : this.selectedLegs) {
        toggleSelection(Leg.legLower());
      }
      break;
    case 'M':
    case 'm':
      for (final Leg Leg : this.selectedLegs) {
        toggleSelection(Leg.middleJoint());
      }
      break;
    case 'U':
    case 'u':
      for (final Leg Leg : this.selectedLegs) {
        toggleSelection(Leg.legUpper());
      }
      break;

    case '7':
      toggleSelection(this.body);
      break;
    


    // change the axis of rotation at current active joint
    case 'X':
    case 'x':
      this.selectedAxis = Axis.X;
      break;
    case 'Y':
    case 'y':
      this.selectedAxis = Axis.Y;
      break;
    case 'Z':
    case 'z':
      this.selectedAxis = Axis.Z;
      break;
    default:
      break;
    }
  }

  /**
   * Prints the joints on the System.out print stream.
   */
  private void printJoints() {
    this.printJoints(System.out);
  }

  /**
   * Prints the joints on the specified PrintStream.
   * 
   * @param printStream
   *          The stream on which to print each of the components.
   */
  private void printJoints(final PrintStream printStream) {
    for (final Component component : this.components) {
      printStream.println(component);
    }
  }

  /**
   * This method is intentionally unimplemented.
   * 
   * @param mouse
   *          This parameter is ignored.
   */
  public void mouseClicked(MouseEvent mouse) {
    // intentionally unimplemented
  }

  /**
   * Updates the rotation quaternion as the mouse is dragged.
   * 
   * @param mouse
   *          The mouse drag event object.
   */
  public void mouseDragged(final MouseEvent mouse) {
	if (this.rotate_world) {
		// get the current position of the mouse
		final int x = mouse.getX();
		final int y = mouse.getY();
	
		// get the change in position from the previous one
		final int dx = x - this.last_x;
		final int dy = y - this.last_y;
	
		// create a unit vector in the direction of the vector (dy, dx, 0)
		final double magnitude = Math.sqrt(dx * dx + dy * dy);
		final float[] axis = magnitude == 0 ? new float[]{1,0,0}: // avoid dividing by 0
			new float[] { (float) (dy / magnitude),(float) (dx / magnitude), 0 };
	
		// calculate appropriate quaternion
		final float viewing_delta = 3.1415927f / 180.0f;
		final float s = (float) Math.sin(0.5f * viewing_delta);
		final float c = (float) Math.cos(0.5f * viewing_delta);
		final Quaternion Q = new Quaternion(c, s * axis[0], s * axis[1], s
				* axis[2]);
		this.viewing_quaternion = Q.multiply(this.viewing_quaternion);
	
		// normalize to counteract acccumulating round-off error
		this.viewing_quaternion.normalize();
	
		// save x, y as last x, y
		this.last_x = x;
		this.last_y = y;
	}
  }

  /**
   * This method is intentionally unimplemented.
   * 
   * @param mouse
   *          This parameter is ignored.
   */
  public void mouseEntered(MouseEvent mouse) {
    // intentionally unimplemented
  }

  /**
   * This method is intentionally unimplemented.
   * 
   * @param mouse
   *          This parameter is ignored.
   */
  public void mouseExited(MouseEvent mouse) {
    // intentionally unimplemented
  }

  /**
   * This method is intentionally unimplemented.
   * 
   * @param mouse
   *          This parameter is ignored.
   */
  public void mouseMoved(MouseEvent mouse) {
    // intentionally unimplemented
  }

  /**
   * Starts rotating the world if the left mouse button was released.
   * 
   * @param mouse
   *          The mouse press event object.
   */
  public void mousePressed(final MouseEvent mouse) {
    if (mouse.getButton() == MouseEvent.BUTTON1) {
      this.last_x = mouse.getX();
      this.last_y = mouse.getY();
      this.rotate_world = true;
    }
  }

  /**
   * Stops rotating the world if the left mouse button was released.
   * 
   * @param mouse
   *          The mouse release event object.
   */
  public void mouseReleased(final MouseEvent mouse) {
    if (mouse.getButton() == MouseEvent.BUTTON1) {
      this.rotate_world = false;
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @param drawable
   *          {@inheritDoc}
   * @param x
   *          {@inheritDoc}
   * @param y
   *          {@inheritDoc}
   * @param width
   *          {@inheritDoc}
   * @param height
   *          {@inheritDoc}
   */
  public void reshape(final GLAutoDrawable drawable, final int x, final int y,
      final int width, final int height) {
    final GL2 gl = (GL2)drawable.getGL();

    // prevent division by zero by ensuring window has height 1 at least
    final int newHeight = Math.max(1, height);

    // compute the aspect ratio
    final double ratio = (double) width / newHeight;

    // reset the projection coordinate system before modifying it
    gl.glMatrixMode(GL2.GL_PROJECTION);
    gl.glLoadIdentity();

    // set the viewport to be the entire window
    gl.glViewport(0, 0, width, newHeight);

    // set the clipping volume
    this.glu.gluPerspective(25, ratio, 0.1, 100);

    // camera positioned at (0,0,6), look at point (0,0,0), up vector (0,1,0)
    this.glu.gluLookAt(0, 0, 12, 0, 0, 0, 0, 1, 0);

    // switch back to model coordinate system
    gl.glMatrixMode(GL2.GL_MODELVIEW);
  }

  private void toggleSelection(final Component component) {
    if (this.selectedComponents.contains(component)) {
      this.selectedComponents.remove(component);
      component.setColor(INACTIVE_COLOR);
    } else {
      this.selectedComponents.add(component);
      component.setColor(ACTIVE_COLOR);
    }
    this.stateChanged = true;
  }

  private void toggleSelection(final Leg Leg) {
    if (this.selectedLegs.contains(Leg)) {
      this.selectedLegs.remove(Leg);
      this.selectedComponents.removeAll(Leg.joints());
      for (final Component joint : Leg.joints()) {
        joint.setColor(INACTIVE_COLOR);
      }
    } else {
      this.selectedLegs.add(Leg);
    }
    this.stateChanged = true;
  }

@Override
public void dispose(GLAutoDrawable drawable) {
	// TODO Auto-generated method stub
	
}
}
