package org.mystify.robo;

import java.awt.geom.Point2D;

import robocode.AdvancedRobot;
import robocode.Condition;
import robocode.CustomEvent;
import robocode.HitByBulletEvent;
import robocode.HitWallEvent;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;

public class MyFirstRobot extends AdvancedRobot {
	private double scanDirection = 1;
	private byte moveDirection = 1;
	private int wallMargin = 60;
	private int tooCloseToWall = 0;
	private Enemy enemy = new Enemy();

	@Override
	public void run() {
		enemy.reset();
		addCustomEvent(new Condition("too_close_to_walls") {
			public boolean test() {
				return (getX() <= wallMargin ||
						getX() >= getBattleFieldWidth() - wallMargin ||
						getY() <= wallMargin ||
						getY() >= getBattleFieldHeight() - wallMargin);
			}
		});

		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);

		while (true) {
			setTurnRadarRight(360);

			if (getTime() % 20 == 0) {
				moveDirection *= -1;
				setAhead(150 * moveDirection);
			}

			if (tooCloseToWall > 0) {
				tooCloseToWall -= 1;
			}

			if (getVelocity() == 0) {
				moveDirection *= -1;
				setAhead(10000 * moveDirection);
			}
		}
	}

	@Override
	public void onScannedRobot(ScannedRobotEvent e) {
		if (enemy.none() || e.getDistance() < enemy.getDistance() - 70 || enemy.getName().equals(e.getName())) {
			enemy.update(e, this);
		}

		scanDirection *= -1;
		setTurnRadarRight(360 * scanDirection);

		if (getGunHeat() == 0 && Math.abs(getGunTurnRemaining()) < 10) {
			double firePower = Math.min(500 / enemy.getDistance(), 3);
			long time = (long) (enemy.getDistance() / 20 - firePower * 3);
			setTurnGunRight(normalizeBearing(absoluteBearing(getX(), getY(), enemy.getFutureX(time), enemy.getFutureY(time)) - getGunHeading()));
			setFire(firePower);
		}
	}

	@Override
	public void onRobotDeath(RobotDeathEvent e) {
		if (e.getName().equals(enemy.getName())) {
			enemy.reset();
		}
	}

	@Override
	public void onHitByBullet(HitByBulletEvent e) {

	}

	@Override
	public void onHitWall(HitWallEvent e) {

	}

	public void onCustomEvent(CustomEvent e) {
		if ("too_close_to_walls".equals(e.getCondition().getName()) && tooCloseToWall <= 0) {
			tooCloseToWall += wallMargin;
			setMaxVelocity(0);
		}
	}

	double normalizeBearing(double angle) {
		while (angle > 180) {
			angle -= 360;
		}

		while (angle < -180) {
			angle += 360;
		}

		return angle;
	}

	double absoluteBearing(double x1, double y1, double x2, double y2) {
		double xo = x2 - x1;
		double yo = y2 - y1;
		double arcSin = Math.toDegrees(Math.asin(xo / Point2D.distance(x1, y1, x2, y2)));
		double bearing = 0;

		if (xo > 0 && yo > 0) {
			bearing = arcSin;
		}
		if (xo < 0 && yo > 0) {
			bearing = 360 + arcSin;
		}
		if (xo > 0 && yo < 0) {
			bearing = 180 - arcSin;
		}
		if (xo < 0 && yo < 0) {
			bearing = 180 - arcSin;
		}

		return bearing;
	}
}
