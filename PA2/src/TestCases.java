/**
 * 
 */


import java.util.HashMap;
import java.util.Map;

/**
 * @author Chanan Suksangium - csuksan@bu.edu
 * @date October 2016
 */
public class TestCases extends CyclicIterator<Map<String, Angled>> {

  Map<String, Angled> stop() {
    return this.stop;
  }

  private final Map<String, Angled> stop;

  @SuppressWarnings("unchecked")
  TestCases() {
    this.stop = new HashMap<String, Angled>();
    final Map<String, Angled> spread = new HashMap<String, Angled>();
    final Map<String, Angled> walk = new HashMap<String, Angled>();
    final Map<String, Angled> curl = new HashMap<String, Angled>();
    final Map<String, Angled> raise = new HashMap<String, Angled>();
    final Map<String, Angled> balance = new HashMap<String, Angled>();

    super.add(stop, spread, walk, curl, raise, balance);

    // angle of body does not change in any of the test cases
    // test cases

    stop.put(PA2.BODY_NAME, new BaseAngled(0, 0, 0));
    spread.put(PA2.BODY_NAME, new BaseAngled(0, 0, 0));
    walk.put(PA2.BODY_NAME, new BaseAngled(0, 0, 0));
    curl.put(PA2.BODY_NAME, new BaseAngled(0, 0, 0));
    raise.put(PA2.BODY_NAME, new BaseAngled(0, 0, 0));
    balance.put(PA2.BODY_NAME, new BaseAngled(0, 0, 0));

    // the stop test case
    //Back left
    stop.put(PA2.BACK_LEFT_LOWER, new BaseAngled(50, 0, 0));
    stop.put(PA2.BACK_LEFT_MIDDLE, new BaseAngled(0, 0, 0));
    stop.put(PA2.BACK_LEFT_UPPER, new BaseAngled(15, -30, 0));
    //middle left
    stop.put(PA2.MIDDLE_LEFT_LOWER, new BaseAngled(50, 0, 0));
    stop.put(PA2.MIDDLE_LEFT_MIDDLE, new BaseAngled(0, 0, 0));
    stop.put(PA2.MIDDLE_LEFT_UPPER, new BaseAngled(15, 0, 0));
    //Front left
    stop.put(PA2.FRONT_LEFT_LOWER, new BaseAngled(50, 0, 0));
    stop.put(PA2.FRONT_LEFT_MIDDLE, new BaseAngled(0, 0, 0));
    stop.put(PA2.FRONT_LEFT_UPPER, new BaseAngled(15, 30, 0));
    
    //Back Right
    stop.put(PA2.BACK_RIGHT_LOWER, new BaseAngled(50, 0, 0));
    stop.put(PA2.BACK_RIGHT_MIDDLE, new BaseAngled(0, 0, 0));
    stop.put(PA2.BACK_RIGHT_UPPER, new BaseAngled(-15, -150, 0));
    
    //Middle Right
    stop.put(PA2.MIDDLE_RIGHT_LOWER, new BaseAngled(50, 0, 0));
    stop.put(PA2.MIDDLE_RIGHT_MIDDLE, new BaseAngled(0, 0, 0));
    stop.put(PA2.MIDDLE_RIGHT_UPPER, new BaseAngled(-15, -180, 0));
    
    //Front Right
    stop.put(PA2.FRONT_RIGHT_LOWER, new BaseAngled(50, 0, 0));
    stop.put(PA2.FRONT_RIGHT_MIDDLE, new BaseAngled(0, 0, 0));
    stop.put(PA2.FRONT_RIGHT_UPPER, new BaseAngled(-15, -210, 0));
   
    
    // the spread legs test case
    //Back Left
    spread.put(PA2.BACK_LEFT_LOWER, new BaseAngled(50, 0, 0));
    spread.put(PA2.BACK_LEFT_MIDDLE, new BaseAngled(0, 0, 0));
    spread.put(PA2.BACK_LEFT_UPPER, new BaseAngled(-20, -50, 0));
 
    spread.put(PA2.MIDDLE_LEFT_LOWER, new BaseAngled(50, 0, 0));
    spread.put(PA2.MIDDLE_LEFT_MIDDLE, new BaseAngled(0, 0, 0));
    spread.put(PA2.MIDDLE_LEFT_UPPER, new BaseAngled(-20, 0, 0));
    
    spread.put(PA2.FRONT_LEFT_LOWER, new BaseAngled(50, 0, 0));
    spread.put(PA2.FRONT_LEFT_MIDDLE, new BaseAngled(0, 0, 0));
    spread.put(PA2.FRONT_LEFT_UPPER, new BaseAngled(-20, 50, 0));
    
    spread.put(PA2.BACK_RIGHT_LOWER, new BaseAngled(50, 0, 0));
    spread.put(PA2.BACK_RIGHT_MIDDLE, new BaseAngled(0, 0, 0));
    spread.put(PA2.BACK_RIGHT_UPPER, new BaseAngled(20, -130, 0));
    
    spread.put(PA2.MIDDLE_RIGHT_LOWER, new BaseAngled(50, 0, 0));
    spread.put(PA2.MIDDLE_RIGHT_MIDDLE, new BaseAngled(0, 0, 0));
    spread.put(PA2.MIDDLE_RIGHT_UPPER, new BaseAngled(20, -180, 0));
    
    spread.put(PA2.FRONT_RIGHT_LOWER, new BaseAngled(50, 0, 0));
    spread.put(PA2.FRONT_RIGHT_MIDDLE, new BaseAngled(0, 0, 0));
    spread.put(PA2.FRONT_RIGHT_UPPER, new BaseAngled(20, -230, 0));
    

    // the walk test case
    //Back left
    walk.put(PA2.BACK_LEFT_LOWER, new BaseAngled(50, 0, 0));
    walk.put(PA2.BACK_LEFT_MIDDLE, new BaseAngled(0, 0, 0));
    walk.put(PA2.BACK_LEFT_UPPER, new BaseAngled(15, 5, 0));
    //middle left
    walk.put(PA2.MIDDLE_LEFT_LOWER, new BaseAngled(50, 0, 0));
    walk.put(PA2.MIDDLE_LEFT_MIDDLE, new BaseAngled(0, 0, 0));
    walk.put(PA2.MIDDLE_LEFT_UPPER, new BaseAngled(15, 20, 0));
    //Front left
    walk.put(PA2.FRONT_LEFT_LOWER, new BaseAngled(50, 0, 0));
    walk.put(PA2.FRONT_LEFT_MIDDLE, new BaseAngled(0, 0, 0));
    walk.put(PA2.FRONT_LEFT_UPPER, new BaseAngled(15, 50, 0));
    
    //Back Right
    walk.put(PA2.BACK_RIGHT_LOWER, new BaseAngled(50, 0, 0));
    walk.put(PA2.BACK_RIGHT_MIDDLE, new BaseAngled(0, 0, 0));
    walk.put(PA2.BACK_RIGHT_UPPER, new BaseAngled(-15, -130, 0));
    
    //Middle Right
    walk.put(PA2.MIDDLE_RIGHT_LOWER, new BaseAngled(50, 0, 0));
    walk.put(PA2.MIDDLE_RIGHT_MIDDLE, new BaseAngled(0, 0, 0));
    walk.put(PA2.MIDDLE_RIGHT_UPPER, new BaseAngled(-15, -150, 0));
    
    //Front Right
    walk.put(PA2.FRONT_RIGHT_LOWER, new BaseAngled(50, 0, 0));
    walk.put(PA2.FRONT_RIGHT_MIDDLE, new BaseAngled(0, 0, 0));
    walk.put(PA2.FRONT_RIGHT_UPPER, new BaseAngled(-15, -180, 0));

    
    
    // the curl test case
    //Back left
    curl.put(PA2.BACK_LEFT_LOWER, new BaseAngled(50, 0, 0));
    curl.put(PA2.BACK_LEFT_MIDDLE, new BaseAngled(45, 0, 0));
    curl.put(PA2.BACK_LEFT_UPPER, new BaseAngled(35, -30, 0));
    //middle left
    curl.put(PA2.MIDDLE_LEFT_LOWER, new BaseAngled(50, 0, 0));
    curl.put(PA2.MIDDLE_LEFT_MIDDLE, new BaseAngled(45, 0, 0));
    curl.put(PA2.MIDDLE_LEFT_UPPER, new BaseAngled(35, 0, 0));
    //Front left
    curl.put(PA2.FRONT_LEFT_LOWER, new BaseAngled(50, 0, 0));
    curl.put(PA2.FRONT_LEFT_MIDDLE, new BaseAngled(45, 0, 0));
    curl.put(PA2.FRONT_LEFT_UPPER, new BaseAngled(35, 30, 0));
    
    //Back Right
    curl.put(PA2.BACK_RIGHT_LOWER, new BaseAngled(50, 0, 0));
    curl.put(PA2.BACK_RIGHT_MIDDLE, new BaseAngled(45, 0, 0));
    curl.put(PA2.BACK_RIGHT_UPPER, new BaseAngled(-35, -150, 0));
    
    //Middle Right
    curl.put(PA2.MIDDLE_RIGHT_LOWER, new BaseAngled(50, 0, 0));
    curl.put(PA2.MIDDLE_RIGHT_MIDDLE, new BaseAngled(45, 0, 0));
    curl.put(PA2.MIDDLE_RIGHT_UPPER, new BaseAngled(-35, -180, 0));
    
    //Front Right
    curl.put(PA2.FRONT_RIGHT_LOWER, new BaseAngled(50, 0, 0));
    curl.put(PA2.FRONT_RIGHT_MIDDLE, new BaseAngled(45, 0, 0));
    curl.put(PA2.FRONT_RIGHT_UPPER, new BaseAngled(-35, -210, 0));

    
    
    // the raise test case
    raise.put(PA2.BACK_LEFT_LOWER, new BaseAngled(50, 0, 0));
    raise.put(PA2.BACK_LEFT_MIDDLE, new BaseAngled(0, 0, 0));
    raise.put(PA2.BACK_LEFT_UPPER, new BaseAngled(15, -30, 0));
    //middle left
    raise.put(PA2.MIDDLE_LEFT_LOWER, new BaseAngled(50, 0, 0));
    raise.put(PA2.MIDDLE_LEFT_MIDDLE, new BaseAngled(0, 0, 0));
    raise.put(PA2.MIDDLE_LEFT_UPPER, new BaseAngled(15, 20, 0));
    //Front left
    raise.put(PA2.FRONT_LEFT_LOWER, new BaseAngled(20, 0, 0));
    raise.put(PA2.FRONT_LEFT_MIDDLE, new BaseAngled(-20, 10, 20));
    raise.put(PA2.FRONT_LEFT_UPPER, new BaseAngled(-5, 50, 0));
    
    //Back Right
    raise.put(PA2.BACK_RIGHT_LOWER, new BaseAngled(50, 0, 0));
    raise.put(PA2.BACK_RIGHT_MIDDLE, new BaseAngled(0, 0, 0));
    raise.put(PA2.BACK_RIGHT_UPPER, new BaseAngled(-15, -150, 0));
    
    //Middle Right
    raise.put(PA2.MIDDLE_RIGHT_LOWER, new BaseAngled(50, 0, 0));
    raise.put(PA2.MIDDLE_RIGHT_MIDDLE, new BaseAngled(0, 0, 0));
    raise.put(PA2.MIDDLE_RIGHT_UPPER, new BaseAngled(-15, -200, 0));
    
    //Front Right
    raise.put(PA2.FRONT_RIGHT_LOWER, new BaseAngled(20, 0, 0));
    raise.put(PA2.FRONT_RIGHT_MIDDLE, new BaseAngled(-20, -10, -20));
    raise.put(PA2.FRONT_RIGHT_UPPER, new BaseAngled(5, -230, 0));
   
    
    // the balance test case
	balance.put(PA2.BACK_LEFT_LOWER, new BaseAngled(30, 0, 0));
    balance.put(PA2.BACK_LEFT_MIDDLE, new BaseAngled(-20, 0, 0));
    balance.put(PA2.BACK_LEFT_UPPER, new BaseAngled(-20, -50, 0));
 
    balance.put(PA2.MIDDLE_LEFT_LOWER, new BaseAngled(30, 0, 0));
    balance.put(PA2.MIDDLE_LEFT_MIDDLE, new BaseAngled(-20, 0, 0));
    balance.put(PA2.MIDDLE_LEFT_UPPER, new BaseAngled(-20, 0, 0));
    
    balance.put(PA2.FRONT_LEFT_LOWER, new BaseAngled(30, 0, 0));
    balance.put(PA2.FRONT_LEFT_MIDDLE, new BaseAngled(-20, 0, 0));
    balance.put(PA2.FRONT_LEFT_UPPER, new BaseAngled(-20, 50, 0));
    //Back Right
    balance.put(PA2.BACK_RIGHT_LOWER, new BaseAngled(30, 0, 0));
    balance.put(PA2.BACK_RIGHT_MIDDLE, new BaseAngled(20, 0, 0));
    balance.put(PA2.BACK_RIGHT_UPPER, new BaseAngled(-35, -150, 0));
    
    //Middle Right
    balance.put(PA2.MIDDLE_RIGHT_LOWER, new BaseAngled(30, 0, 0));
    balance.put(PA2.MIDDLE_RIGHT_MIDDLE, new BaseAngled(20, 0, 0));
    balance.put(PA2.MIDDLE_RIGHT_UPPER, new BaseAngled(-35, -180, 0));
    
    //Front Right
    balance.put(PA2.FRONT_RIGHT_LOWER, new BaseAngled(30, 0, 0));
    balance.put(PA2.FRONT_RIGHT_MIDDLE, new BaseAngled(20, 0, 0));
    balance.put(PA2.FRONT_RIGHT_UPPER, new BaseAngled(-35, -210, 0));
   
  }
}
