/*
 * Dotger - A game where you have to dodge objects.
 * Copyright (C) 2023  Michael Pütz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package game;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static game.Helpers.*;

public final class CircleAngle {

	private static final BigDecimal BIG_PI = new BigDecimal(Math.PI);
	public static final CircleAngle DEGREE_0 = new CircleAngle(0);
	public static final CircleAngle DEGREE_45 = new CircleAngle(BIG_PI.multiply(new BigDecimal("0.25")));
	public static final CircleAngle DEGREE_90 = new CircleAngle(BIG_PI.multiply(new BigDecimal("0.5")));
	public static final CircleAngle DEGREE_135 = new CircleAngle(BIG_PI.multiply(new BigDecimal("0.75")));
	public static final CircleAngle DEGREE_180 = new CircleAngle(BIG_PI);
	public static final CircleAngle DEGREE_225 = new CircleAngle(BIG_PI.multiply(new BigDecimal("1.25")));
	public static final CircleAngle DEGREE_270 = new CircleAngle(BIG_PI.multiply(new BigDecimal("1.5")));
	public static final CircleAngle DEGREE_315 = new CircleAngle(BIG_PI.multiply(new BigDecimal("1.75")));
	
	private final BigDecimal angle;
	
	public CircleAngle() {
		this.angle = new BigDecimal(0);
	}
	
	public CircleAngle(double angle) {
		this.angle = getAngleInRange(new BigDecimal(angle));
	}

	public CircleAngle(BigDecimal angle) {
		this.angle = getAngleInRange(angle);
	}
	
	public static CircleAngle smallerAngleBetween(CircleAngle angle, CircleAngle otherAngle) {
		double difference = Math.abs(angle.get() - otherAngle.get());
		if (difference > DEGREE_180.get()) {
			difference = -difference;
		}
		return new CircleAngle(difference);
	}

	public static CircleAngle oppositeAngle(CircleAngle angle) {
		return angle.add(DEGREE_180);
	}

	public static CircleAngle randomAngle() {
		return new CircleAngle(RANDOM.nextDouble(Math.PI * 2));
	}

	public CircleAngle add(CircleAngle otherAngle) {
		return new CircleAngle(angle.add(otherAngle.getBigDecimalValue()));
	}

	public CircleAngle subtract(CircleAngle otherAngle) {
		return new CircleAngle(angle.subtract(otherAngle.getBigDecimalValue()));
	}

	public CircleAngle multiply(double multiplier) {
		return new CircleAngle(angle.multiply(new BigDecimal(multiplier)));
	}

	public CircleAngle divide(double divisor) {
		return new CircleAngle(angle.divide(new BigDecimal(divisor), 50, RoundingMode.HALF_UP));
	}

	public boolean isBetween(CircleAngle firstAngle, CircleAngle secondAngle) {
		if (firstAngle.get() >= secondAngle.get()) {
			BigDecimal fullAngle = BIG_PI.multiply(new BigDecimal("2"));
			double lowestAngle = 0.0;
			return (angle.doubleValue() > firstAngle.get() && angle.doubleValue() <= fullAngle.doubleValue())
					|| (angle.doubleValue() >= lowestAngle && angle.doubleValue() < secondAngle.get());
		} else {
			return angle.doubleValue() > firstAngle.get() && angle.doubleValue() < secondAngle.get();
		}
	}

	public boolean isBetweenOrEqual(CircleAngle firstAngle, CircleAngle secondAngle) {
		if (firstAngle.get() >= secondAngle.get()) {
			BigDecimal fullAngle = BIG_PI.multiply(new BigDecimal("2"));
			double lowestAngle = 0.0;
			return (angle.doubleValue() >= firstAngle.get() && angle.doubleValue() <= fullAngle.doubleValue())
					|| (angle.doubleValue() >= lowestAngle && angle.doubleValue() <= secondAngle.get());
		} else {
			return angle.doubleValue() >= firstAngle.get() && angle.doubleValue() <= secondAngle.get();
		}
	}

	private BigDecimal getAngleInRange(BigDecimal angle) {
		BigDecimal degree360 = new BigDecimal(Math.PI).multiply(new BigDecimal("2"));
		BigDecimal remainderAngle = angle.remainder(degree360);
		if (angle.compareTo(new BigDecimal("0")) < 0) {
			return degree360.subtract(remainderAngle.abs());
		} else {
			return remainderAngle;
		}
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		} else if (this.getClass() != object.getClass()) {
			return false;
		}
		CircleAngle other = (CircleAngle) object;
		return this.getBigDecimalValue().compareTo(other.getBigDecimalValue()) == 0;
	}
	
	public double get() {
		return angle.doubleValue();
	}

	private BigDecimal getBigDecimalValue() {
		return angle;
	}

}
