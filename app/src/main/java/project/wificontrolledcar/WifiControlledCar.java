package project.wificontrolledcar;

/**
 * WifiControlledCar.java
 *
 * This class is for keeping track of the Arduino-car. It contains methods such as methods for
 * getting current speeds and a string to be sent to the Arduino-car.
 *
 * @author Jonas Eiselt
 * @since 2016-05-20
 */
public class WifiControlledCar
{
    private int forwardSpeed;
    private int backwardsSpeed;

    private int left;
    private int right;

    private int forwardTimes;
    private int backwardsTimes;

    public WifiControlledCar()
    {
        // Empty constructor
    }

    /**
     * @return speed between 0 and 255
     */
    public int getForwardSpeed()
    {
        return forwardSpeed;
    }

    /**
     * @return speed between 0 and 255
     */
    public int getBackwardsSpeed()
    {
        return backwardsSpeed;
    }

    /**
     * @return left, 0 or 1
     */
    public int getLeft()
    {
        return left;
    }

    /**
     * @return right, 0 or 1
     */
    public int getRight()
    {
        return right;
    }

    /**
     * This is a public method that increases speed forward if current speed forward is greater or
     * equal to 0 and less than 255, or decreases the speed backwards if that speed is greater than
     * 0.
     */
    public void accelerate()
    {
        if (backwardsSpeed < 0)
        {
            if (backwardsTimes == 255)
            {
                backwardsSpeed -= 55;
                backwardsTimes--;
            }
            else if (backwardsTimes > 0)
            {
                backwardsSpeed -= 50;
                backwardsTimes--;
            }
        }
        else
        {
            if (forwardTimes >= 0 && forwardTimes < 4)
            {
                forwardSpeed += 50;
                forwardTimes++;
            }
            else if (forwardTimes == 4)
            {
                forwardSpeed += 55;
                forwardTimes++;
            }
        }
    }

    /**
     * This is a public method that increases speed backwards if current speed backwards is greater
     * or equal to 0 and less than 255, or decreases the speed forward if that speed is greater than
     * 0.
     */
    public void retard()
    {
        if (forwardTimes > 0 && forwardTimes <= 5)
        {
            if (forwardTimes == 5)
            {
                forwardSpeed -= 55;
                forwardTimes--;
            }
            else if (forwardTimes > 0)
            {
                forwardSpeed -= 50;
                forwardTimes--;
            }
        }
        else
        {
            if (backwardsTimes >= 0 && backwardsTimes < 4)
            {
                backwardsSpeed += 50;
                backwardsTimes++;
            }
            else if (backwardsTimes == 4)
            {
                backwardsSpeed += 55;
                backwardsTimes++;
            }
        }
    }

    /**
     * This is a public method that sets left variable to 0 if it was previously 1, otherwise the
     * right variable will be set to 1.
     */
    public void turnRight()
    {
        if(left == 1)
        {
            left = 0;
            right = 0;
        }
        else
        {
            right = 1;
        }
    }

    /**
     * This is a public method that sets right variable to 0 if it was previously 1, otherwise the
     * left variable will be set to 1.
     */
    public void turnLeft()
    {
        if(right == 1)
        {
            right = 0;
            left = 0;
        }
        else
        {
            left = 1;
        }
    }

    /**
     * This is a public method that turns off motor by setting the two variables forwardSpeed and
     * backwardsSpeed to 0.
     */
    public void brake()
    {
        forwardSpeed = 0;
        backwardsSpeed = 0;

        forwardTimes = 0;
        backwardsTimes = 0;
    }

    /**
     * This is a public method that calculates and returns the percentage of current speed and 255.
     * If forwardSpeed is greater than 0, a positive percentage will be returned. If backwardsSpeed
     * is greater than 0, a negative percentage will be returned.
     * @return current percentage speed
     */
    public int getPercentage()
    {
        if(forwardSpeed > 0)
        {
            return ((forwardSpeed*100)/255);
        }
        else if(backwardsSpeed > 0)
        {
            return -((backwardsSpeed*100)/255);
        }
        return 0;
    }

    /**
     * This is a public method that returns the string to be sent to server and processed by
     * Arduino.
     * @return string with all speeds
     */
    public String getSpeedValues()
    {
        return "dx " + forwardSpeed + ":" + right + ":" + backwardsSpeed + ":" + left;
    }
}