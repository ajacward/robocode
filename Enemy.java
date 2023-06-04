package org.mystify.robo;

import robocode.Robot;
import robocode.ScannedRobotEvent;

public class Enemy {
  private double x;
  private double y;
  private double bearing;
  private double distance;
  private double energy;
  private double heading;
  private String name;
  private double velocity;

  public Enemy() {
    this.name = "";
  }

  public void reset() {
    x = 0.0;
    y = 0.0;
    bearing = 0.0;
    distance = 0.0;
    energy = 0.0;
    heading = 0.0;
    name = "";
    velocity = 0.0;
  }

  public boolean none() {
    return "".equals(name);
  }

  public void update(ScannedRobotEvent e, Robot robot) {
    bearing = e.getBearing();
    distance = e.getDistance();
    energy = e.getEnergy();
    heading = e.getHeading();
    name = e.getName();
    velocity = e.getVelocity();

    double absBearingDeg = (robot.getHeading() + e.getBearing());

    if (absBearingDeg < 0) {
      absBearingDeg += 360;
    }

    x = robot.getX() + Math.sin(Math.toRadians(absBearingDeg)) * e.getDistance();
    y = robot.getY() + Math.cos(Math.toRadians(absBearingDeg)) * e.getDistance();
  }

  public double getFutureX(long when) {
    return x + Math.sin(Math.toRadians(getHeading())) * getVelocity() * when;
  }

  public double getFutureY(long when) {
    return y + Math.cos(Math.toRadians(getHeading())) * getVelocity() * when;
  }

  public double getX() {
    return x;
  }

  public void setX(double x) {
    this.x = x;
  }

  public double getY() {
    return y;
  }

  public void setY(double y) {
    this.y = y;
  }

  public double getBearing() {
    return bearing;
  }

  public void setBearing(double bearing) {
    this.bearing = bearing;
  }

  public double getDistance() {
    return distance;
  }

  public void setDistance(double distance) {
    this.distance = distance;
  }

  public double getEnergy() {
    return energy;
  }

  public void setEnergy(double energy) {
    this.energy = energy;
  }

  public double getHeading() {
    return heading;
  }

  public void setHeading(double heading) {
    this.heading = heading;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getVelocity() {
    return velocity;
  }

  public void setVelocity(double velocity) {
    this.velocity = velocity;
  }
}
