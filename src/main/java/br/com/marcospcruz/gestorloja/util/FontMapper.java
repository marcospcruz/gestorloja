package br.com.marcospcruz.gestorloja.util;

import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

public class FontMapper {

	static final Map<Integer, Font> FONT_MAP = createMap();

	private FontMapper() {
	}

	private static Map<Integer, Font> createMap() {
		Map<Integer, Font> map = new HashMap<>();
		map.put(15, new Font("Tahoma", Font.PLAIN, 15));
		map.put(22, new Font("Tahoma", Font.PLAIN, 22));
		map.put(20, new Font("Tahoma", Font.PLAIN, 20));
		map.put(25, new Font("Tahoma", Font.PLAIN, 25));
		map.put(30, new Font("Tahoma", Font.PLAIN, 30));
		return map;
	}

	public static Font getFont(int size) {
		size = 15;
		return FONT_MAP.get(size);
	}

}
