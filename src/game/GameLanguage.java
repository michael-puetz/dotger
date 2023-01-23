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

import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

public class GameLanguage {
	
	private static final GameLanguage INSTANCE = new GameLanguage();
	private static final String BUNDLE_NAME = "Dotger";
	
	public static GameLanguage getInstance() {
		return INSTANCE;
	}
	
	private ResourceBundle bundle;
	private final Set<LanguageSwitcher> switcherToNotify = new HashSet<>();
	
	private GameLanguage() {
		bundle = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault());
	}
	
	public String getString(String key) {
		return bundle.getString(key);
	}
	
	public void switchToLanguage(Locale newLanguage) {
		bundle = ResourceBundle.getBundle(BUNDLE_NAME, newLanguage);
		for (LanguageSwitcher switcher : switcherToNotify) {
			switcher.updateLanguage();
		}
	}
	
	public boolean register(LanguageSwitcher switcher) {
		return switcherToNotify.add(switcher);
	}
	
}
