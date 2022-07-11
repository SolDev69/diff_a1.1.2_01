package net.minecraft.src;

public class MathHelper {
	private static float[] SIN_TABLE = new float[65536];

	public static final float sin(float floatValue) {
		return SIN_TABLE[(int)(floatValue * 10430.378F) & '\uffff'];
	}

	public static final float cos(float floatValue) {
		return SIN_TABLE[(int)(floatValue * 10430.378F + 16384.0F) & '\uffff'];
	}

	public static final float sqrt_float(float floatValue) {
		return (float)Math.sqrt((double)floatValue);
	}

	public static final float sqrt_double(double doubleValue) {
		return (float)Math.sqrt(doubleValue);
	}

	public static int floor_float(float floatValue) {
		int var1 = (int)floatValue;
		return floatValue < (float)var1 ? var1 - 1 : var1;
	}

	public static int floor_double(double doubleValue) {
		int var2 = (int)doubleValue;
		return doubleValue < (double)var2 ? var2 - 1 : var2;
	}

	public static float abs(float floatValue) {
		return floatValue >= 0.0F ? floatValue : -floatValue;
	}

	public static double abs_max(double doubleValue1, double doubleValue2) {
		if(doubleValue1 < 0.0D) {
			doubleValue1 = -doubleValue1;
		}

		if(doubleValue2 < 0.0D) {
			doubleValue2 = -doubleValue2;
		}

		return doubleValue1 > doubleValue2 ? doubleValue1 : doubleValue2;
	}

	static {
		for(int var0 = 0; var0 < 65536; ++var0) {
			SIN_TABLE[var0] = (float)Math.sin((double)var0 * Math.PI * 2.0D / 65536.0D);
		}

	}
}
