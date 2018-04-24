/*
 * This file is part of NanoUI
 * 
 * Copyright (C) 2017-2018 Guerra24
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package net.luxvacuos.nanoui.framehost;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Structure;
import com.sun.jna.platform.unix.X11.Cursor;
import com.sun.jna.platform.unix.X11.Display;
import com.sun.jna.platform.unix.X11.Window;
import com.sun.jna.platform.unix.X11.XEvent;

public interface X11Ext extends Library {
	public static X11Ext INSTANCE = (X11Ext) Native.loadLibrary("X11", X11Ext.class);

	public int XConfigureWindow(Display display, Window window, NativeLong value_mask, XWindowChanges changes);

	public int XAddToSaveSet(Display display, Window window);

	public int XRemoveFromSaveSet(Display display, Window window);

	public int XReparentWindow(Display display, Window w, Window parent, int x, int y);

	public int XGrabServer(Display display);

	public int XUngrabServer(Display display);

	public int XGrabButton(Display display, int button, int modifiers, Window grab_window, boolean owner_events,
			int event_mask, int pointer_mode, int keyboard_mode, Window confine_to, Cursor cursor);

	public int XRaiseWindow(Display display, Window window);

	public int XMoveWindow(Display display, Window w, int x, int y);

	public int XResizeWindow(Display display, Window w, int width, int height);

	public int XNextEvent(Display display, XEvent event_return);

	public boolean XCheckTypedWindowEvent(Display display, Window w, int event_type, XEvent event_return);

	public class XWindowChanges extends Structure {
		private static final List<String> FIELDS = Arrays.asList("x", "y", "width", "height", "border_width", "sibling",
				"stack_mode");

		public int x;
		public int y;
		public int width;
		public int height;
		public int border_width;
		public Window sibling;
		public int stack_mode;

		@Override
		protected List<String> getFieldOrder() {
			return FIELDS;
		}
	}

	/*
	 * TTY function keys, cleverly chosen to map to ASCII, for convenience of
	 * programming, but could have been arbitrary (at the cost of lookup tables in
	 * client code).
	 */

	public static final int XK_BackSpace = 0xff08; /* Back space, back char */
	public static final int XK_Tab = 0xff09;
	public static final int XK_Linefeed = 0xff0a; /* Linefeed, LF */
	public static final int XK_Clear = 0xff0b;
	public static final int XK_Return = 0xff0d; /* Return, enter */
	public static final int XK_Pause = 0xff13; /* Pause, hold */
	public static final int XK_Scroll_Lock = 0xff14;
	public static final int XK_Sys_Req = 0xff15;
	public static final int XK_Escape = 0xff1b;
	public static final int XK_Delete = 0xffff; /* Delete, rubout */

	/* Cursor control & motion */

	public static final int XK_Home = 0xff50;
	public static final int XK_Left = 0xff51; /* Move left, left arrow */
	public static final int XK_Up = 0xff52; /* Move up, up arrow */
	public static final int XK_Right = 0xff53; /* Move right, right arrow */
	public static final int XK_Down = 0xff54; /* Move down, down arrow */
	public static final int XK_Prior = 0xff55; /* Prior, previous */
	public static final int XK_Page_Up = 0xff55;
	public static final int XK_Next = 0xff56; /* Next */
	public static final int XK_Page_Down = 0xff56;
	public static final int XK_End = 0xff57; /* EOL */
	public static final int XK_Begin = 0xff58; /* BOL */

	/* Misc functions */

	public static final int XK_Select = 0xff60; /* Select, mark */
	public static final int XK_Print = 0xff61;
	public static final int XK_Execute = 0xff62; /* Execute, run, do */
	public static final int XK_Insert = 0xff63; /* Insert, insert here */
	public static final int XK_Undo = 0xff65;
	public static final int XK_Redo = 0xff66; /* Redo, again */
	public static final int XK_Menu = 0xff67;
	public static final int XK_Find = 0xff68; /* Find, search */
	public static final int XK_Cancel = 0xff69; /* Cancel, stop, abort, exit */
	public static final int XK_Help = 0xff6a; /* Help */
	public static final int XK_Break = 0xff6b;
	public static final int XK_Mode_switch = 0xff7e; /* Character set switch */
	public static final int XK_script_switch = 0xff7e; /*
														 * Alias for mode_switch
														 */
	public static final int XK_Num_Lock = 0xff7f;

	/* Keypad functions, keypad numbers cleverly chosen to map to ASCII */

	public static final int XK_KP_Space = 0xff80; /* Space */
	public static final int XK_KP_Tab = 0xff89;
	public static final int XK_KP_Enter = 0xff8d; /* Enter */
	public static final int XK_KP_F1 = 0xff91; /* PF1, KP_A, ... */
	public static final int XK_KP_F2 = 0xff92;
	public static final int XK_KP_F3 = 0xff93;
	public static final int XK_KP_F4 = 0xff94;
	public static final int XK_KP_Home = 0xff95;
	public static final int XK_KP_Left = 0xff96;
	public static final int XK_KP_Up = 0xff97;
	public static final int XK_KP_Right = 0xff98;
	public static final int XK_KP_Down = 0xff99;
	public static final int XK_KP_Prior = 0xff9a;
	public static final int XK_KP_Page_Up = 0xff9a;
	public static final int XK_KP_Next = 0xff9b;
	public static final int XK_KP_Page_Down = 0xff9b;
	public static final int XK_KP_End = 0xff9c;
	public static final int XK_KP_Begin = 0xff9d;
	public static final int XK_KP_Insert = 0xff9e;
	public static final int XK_KP_Delete = 0xff9f;
	public static final int XK_KP_Equal = 0xffbd; /* Equals */
	public static final int XK_KP_Multiply = 0xffaa;
	public static final int XK_KP_Add = 0xffab;
	public static final int XK_KP_Separator = 0xffac; /*
														 * Separator, often comma
														 */
	public static final int XK_KP_Subtract = 0xffad;
	public static final int XK_KP_Decimal = 0xffae;
	public static final int XK_KP_Divide = 0xffaf;

	public static final int XK_KP_0 = 0xffb0;
	public static final int XK_KP_1 = 0xffb1;
	public static final int XK_KP_2 = 0xffb2;
	public static final int XK_KP_3 = 0xffb3;
	public static final int XK_KP_4 = 0xffb4;
	public static final int XK_KP_5 = 0xffb5;
	public static final int XK_KP_6 = 0xffb6;
	public static final int XK_KP_7 = 0xffb7;
	public static final int XK_KP_8 = 0xffb8;
	public static final int XK_KP_9 = 0xffb9;

	/*
	 * Auxilliary functions; note the duplicate definitions for left and right
	 * function keys; Sun keyboards and a few other manufactures have such function
	 * key groups on the left and/or right sides of the keyboard. We've not found a
	 * keyboard with more than 35 function keys total.
	 */

	public static final int XK_F1 = 0xffbe;
	public static final int XK_F2 = 0xffbf;
	public static final int XK_F3 = 0xffc0;
	public static final int XK_F4 = 0xffc1;
	public static final int XK_F5 = 0xffc2;
	public static final int XK_F6 = 0xffc3;
	public static final int XK_F7 = 0xffc4;
	public static final int XK_F8 = 0xffc5;
	public static final int XK_F9 = 0xffc6;
	public static final int XK_F10 = 0xffc7;
	public static final int XK_F11 = 0xffc8;
	public static final int XK_L1 = 0xffc8;
	public static final int XK_F12 = 0xffc9;
	public static final int XK_L2 = 0xffc9;
	public static final int XK_F13 = 0xffca;
	public static final int XK_L3 = 0xffca;
	public static final int XK_F14 = 0xffcb;
	public static final int XK_L4 = 0xffcb;
	public static final int XK_F15 = 0xffcc;
	public static final int XK_L5 = 0xffcc;
	public static final int XK_F16 = 0xffcd;
	public static final int XK_L6 = 0xffcd;
	public static final int XK_F17 = 0xffce;
	public static final int XK_L7 = 0xffce;
	public static final int XK_F18 = 0xffcf;
	public static final int XK_L8 = 0xffcf;
	public static final int XK_F19 = 0xffd0;
	public static final int XK_L9 = 0xffd0;
	public static final int XK_F20 = 0xffd1;
	public static final int XK_L10 = 0xffd1;
	public static final int XK_F21 = 0xffd2;
	public static final int XK_R1 = 0xffd2;
	public static final int XK_F22 = 0xffd3;
	public static final int XK_R2 = 0xffd3;
	public static final int XK_F23 = 0xffd4;
	public static final int XK_R3 = 0xffd4;
	public static final int XK_F24 = 0xffd5;
	public static final int XK_R4 = 0xffd5;
	public static final int XK_F25 = 0xffd6;
	public static final int XK_R5 = 0xffd6;
	public static final int XK_F26 = 0xffd7;
	public static final int XK_R6 = 0xffd7;
	public static final int XK_F27 = 0xffd8;
	public static final int XK_R7 = 0xffd8;
	public static final int XK_F28 = 0xffd9;
	public static final int XK_R8 = 0xffd9;
	public static final int XK_F29 = 0xffda;
	public static final int XK_R9 = 0xffda;
	public static final int XK_F30 = 0xffdb;
	public static final int XK_R10 = 0xffdb;
	public static final int XK_F31 = 0xffdc;
	public static final int XK_R11 = 0xffdc;
	public static final int XK_F32 = 0xffdd;
	public static final int XK_R12 = 0xffdd;
	public static final int XK_F33 = 0xffde;
	public static final int XK_R13 = 0xffde;
	public static final int XK_F34 = 0xffdf;
	public static final int XK_R14 = 0xffdf;
	public static final int XK_F35 = 0xffe0;
	public static final int XK_R15 = 0xffe0;

	/* Modifiers */

	public static final int XK_Shift_L = 0xffe1; /* Left shift */
	public static final int XK_Shift_R = 0xffe2; /* Right shift */
	public static final int XK_Control_L = 0xffe3; /* Left control */
	public static final int XK_Control_R = 0xffe4; /* Right control */
	public static final int XK_Caps_Lock = 0xffe5; /* Caps lock */
	public static final int XK_Shift_Lock = 0xffe6; /* Shift lock */

	public static final int XK_Meta_L = 0xffe7; /* Left meta */
	public static final int XK_Meta_R = 0xffe8; /* Right meta */
	public static final int XK_Alt_L = 0xffe9; /* Left alt */
	public static final int XK_Alt_R = 0xffea; /* Right alt */
	public static final int XK_Super_L = 0xffeb; /* Left super */
	public static final int XK_Super_R = 0xffec; /* Right super */
	public static final int XK_Hyper_L = 0xffed; /* Left hyper */
	public static final int XK_Hyper_R = 0xffee; /* Right hyper */
	/* XK_MISCELLANY */

	/*
	 * Latin 1 (ISO/IEC 8859-1 = Unicode U+0020..U+00FF) Byte 3 = 0
	 */
	public static final int XK_space = 0x0020; /* U+0020 SPACE */
	public static final int XK_exclam = 0x0021; /* U+0021 EXCLAMATION MARK */
	public static final int XK_quotedbl = 0x0022; /* U+0022 QUOTATION MARK */
	public static final int XK_numbersign = 0x0023; /* U+0023 NUMBER SIGN */
	public static final int XK_dollar = 0x0024; /* U+0024 DOLLAR SIGN */
	public static final int XK_percent = 0x0025; /* U+0025 PERCENT SIGN */
	public static final int XK_ampersand = 0x0026; /* U+0026 AMPERSAND */
	public static final int XK_apostrophe = 0x0027; /* U+0027 APOSTROPHE */
	public static final int XK_quoteright = 0x0027; /* deprecated */
	public static final int XK_parenleft = 0x0028; /* U+0028 LEFT PARENTHESIS */
	public static final int XK_parenright = 0x0029; /*
													 * U+0029 RIGHT PARENTHESIS
													 */
	public static final int XK_asterisk = 0x002a; /* U+002A ASTERISK */
	public static final int XK_plus = 0x002b; /* U+002B PLUS SIGN */
	public static final int XK_comma = 0x002c; /* U+002C COMMA */
	public static final int XK_minus = 0x002d; /* U+002D HYPHEN-MINUS */
	public static final int XK_period = 0x002e; /* U+002E FULL STOP */
	public static final int XK_slash = 0x002f; /* U+002F SOLIDUS */
	public static final int XK_0 = 0x0030; /* U+0030 DIGIT ZERO */
	public static final int XK_1 = 0x0031; /* U+0031 DIGIT ONE */
	public static final int XK_2 = 0x0032; /* U+0032 DIGIT TWO */
	public static final int XK_3 = 0x0033; /* U+0033 DIGIT THREE */
	public static final int XK_4 = 0x0034; /* U+0034 DIGIT FOUR */
	public static final int XK_5 = 0x0035; /* U+0035 DIGIT FIVE */
	public static final int XK_6 = 0x0036; /* U+0036 DIGIT SIX */
	public static final int XK_7 = 0x0037; /* U+0037 DIGIT SEVEN */
	public static final int XK_8 = 0x0038; /* U+0038 DIGIT EIGHT */
	public static final int XK_9 = 0x0039; /* U+0039 DIGIT NINE */
	public static final int XK_colon = 0x003a; /* U+003A COLON */
	public static final int XK_semicolon = 0x003b; /* U+003B SEMICOLON */
	public static final int XK_less = 0x003c; /* U+003C LESS-THAN SIGN */
	public static final int XK_equal = 0x003d; /* U+003D EQUALS SIGN */
	public static final int XK_greater = 0x003e; /* U+003E GREATER-THAN SIGN */
	public static final int XK_question = 0x003f; /* U+003F QUESTION MARK */
	public static final int XK_at = 0x0040; /* U+0040 COMMERCIAL AT */
	public static final int XK_A = 0x0041; /* U+0041 LATIN CAPITAL LETTER A */
	public static final int XK_B = 0x0042; /* U+0042 LATIN CAPITAL LETTER B */
	public static final int XK_C = 0x0043; /* U+0043 LATIN CAPITAL LETTER C */
	public static final int XK_D = 0x0044; /* U+0044 LATIN CAPITAL LETTER D */
	public static final int XK_E = 0x0045; /* U+0045 LATIN CAPITAL LETTER E */
	public static final int XK_F = 0x0046; /* U+0046 LATIN CAPITAL LETTER F */
	public static final int XK_G = 0x0047; /* U+0047 LATIN CAPITAL LETTER G */
	public static final int XK_H = 0x0048; /* U+0048 LATIN CAPITAL LETTER H */
	public static final int XK_I = 0x0049; /* U+0049 LATIN CAPITAL LETTER I */
	public static final int XK_J = 0x004a; /* U+004A LATIN CAPITAL LETTER J */
	public static final int XK_K = 0x004b; /* U+004B LATIN CAPITAL LETTER K */
	public static final int XK_L = 0x004c; /* U+004C LATIN CAPITAL LETTER L */
	public static final int XK_M = 0x004d; /* U+004D LATIN CAPITAL LETTER M */
	public static final int XK_N = 0x004e; /* U+004E LATIN CAPITAL LETTER N */
	public static final int XK_O = 0x004f; /* U+004F LATIN CAPITAL LETTER O */
	public static final int XK_P = 0x0050; /* U+0050 LATIN CAPITAL LETTER P */
	public static final int XK_Q = 0x0051; /* U+0051 LATIN CAPITAL LETTER Q */
	public static final int XK_R = 0x0052; /* U+0052 LATIN CAPITAL LETTER R */
	public static final int XK_S = 0x0053; /* U+0053 LATIN CAPITAL LETTER S */
	public static final int XK_T = 0x0054; /* U+0054 LATIN CAPITAL LETTER T */
	public static final int XK_U = 0x0055; /* U+0055 LATIN CAPITAL LETTER U */
	public static final int XK_V = 0x0056; /* U+0056 LATIN CAPITAL LETTER V */
	public static final int XK_W = 0x0057; /* U+0057 LATIN CAPITAL LETTER W */
	public static final int XK_X = 0x0058; /* U+0058 LATIN CAPITAL LETTER X */
	public static final int XK_Y = 0x0059; /* U+0059 LATIN CAPITAL LETTER Y */
	public static final int XK_Z = 0x005a; /* U+005A LATIN CAPITAL LETTER Z */
	public static final int XK_bracketleft = 0x005b; /*
														 * U+005B LEFT SQUARE BRACKET
														 */
	public static final int XK_backslash = 0x005c; /* U+005C REVERSE SOLIDUS */
	public static final int XK_bracketright = 0x005d; /*
														 * U+005D RIGHT SQUARE BRACKET
														 */
	public static final int XK_asciicircum = 0x005e; /*
														 * U+005E CIRCUMFLEX ACCENT
														 */
	public static final int XK_underscore = 0x005f; /* U+005F LOW LINE */
	public static final int XK_grave = 0x0060; /* U+0060 GRAVE ACCENT */
	public static final int XK_quoteleft = 0x0060; /* deprecated */
	public static final int XK_a = 0x0061; /* U+0061 LATIN SMALL LETTER A */
	public static final int XK_b = 0x0062; /* U+0062 LATIN SMALL LETTER B */
	public static final int XK_c = 0x0063; /* U+0063 LATIN SMALL LETTER C */
	public static final int XK_d = 0x0064; /* U+0064 LATIN SMALL LETTER D */
	public static final int XK_e = 0x0065; /* U+0065 LATIN SMALL LETTER E */
	public static final int XK_f = 0x0066; /* U+0066 LATIN SMALL LETTER F */
	public static final int XK_g = 0x0067; /* U+0067 LATIN SMALL LETTER G */
	public static final int XK_h = 0x0068; /* U+0068 LATIN SMALL LETTER H */
	public static final int XK_i = 0x0069; /* U+0069 LATIN SMALL LETTER I */
	public static final int XK_j = 0x006a; /* U+006A LATIN SMALL LETTER J */
	public static final int XK_k = 0x006b; /* U+006B LATIN SMALL LETTER K */
	public static final int XK_l = 0x006c; /* U+006C LATIN SMALL LETTER L */
	public static final int XK_m = 0x006d; /* U+006D LATIN SMALL LETTER M */
	public static final int XK_n = 0x006e; /* U+006E LATIN SMALL LETTER N */
	public static final int XK_o = 0x006f; /* U+006F LATIN SMALL LETTER O */
	public static final int XK_p = 0x0070; /* U+0070 LATIN SMALL LETTER P */
	public static final int XK_q = 0x0071; /* U+0071 LATIN SMALL LETTER Q */
	public static final int XK_r = 0x0072; /* U+0072 LATIN SMALL LETTER R */
	public static final int XK_s = 0x0073; /* U+0073 LATIN SMALL LETTER S */
	public static final int XK_t = 0x0074; /* U+0074 LATIN SMALL LETTER T */
	public static final int XK_u = 0x0075; /* U+0075 LATIN SMALL LETTER U */
	public static final int XK_v = 0x0076; /* U+0076 LATIN SMALL LETTER V */
	public static final int XK_w = 0x0077; /* U+0077 LATIN SMALL LETTER W */
	public static final int XK_x = 0x0078; /* U+0078 LATIN SMALL LETTER X */
	public static final int XK_y = 0x0079; /* U+0079 LATIN SMALL LETTER Y */
	public static final int XK_z = 0x007a; /* U+007A LATIN SMALL LETTER Z */
	public static final int XK_braceleft = 0x007b; /*
													 * U+007B LEFT CURLY BRACKET
													 */
	public static final int XK_bar = 0x007c; /* U+007C VERTICAL LINE */
	public static final int XK_braceright = 0x007d; /*
													 * U+007D RIGHT CURLY BRACKET
													 */
	public static final int XK_asciitilde = 0x007e; /* U+007E TILDE */

	public static final int XK_nobreakspace = 0x00a0; /*
														 * U+00A0 NO-BREAK SPACE
														 */
	public static final int XK_exclamdown = 0x00a1; /*
													 * U+00A1 INVERTED EXCLAMATION MARK
													 */
	public static final int XK_cent = 0x00a2; /* U+00A2 CENT SIGN */
	public static final int XK_sterling = 0x00a3; /* U+00A3 POUND SIGN */
	public static final int XK_currency = 0x00a4; /* U+00A4 CURRENCY SIGN */
	public static final int XK_yen = 0x00a5; /* U+00A5 YEN SIGN */
	public static final int XK_brokenbar = 0x00a6; /* U+00A6 BROKEN BAR */
	public static final int XK_section = 0x00a7; /* U+00A7 SECTION SIGN */
	public static final int XK_diaeresis = 0x00a8; /* U+00A8 DIAERESIS */
	public static final int XK_copyright = 0x00a9; /* U+00A9 COPYRIGHT SIGN */
	public static final int XK_ordfeminine = 0x00aa; /*
														 * U+00AA FEMININE ORDINAL INDICATOR
														 */
	public static final int XK_guillemotleft = 0x00ab; /*
														 * U+00AB LEFT-POINTING DOUBLE ANGLE QUOTATION MARK
														 */
	public static final int XK_notsign = 0x00ac; /* U+00AC NOT SIGN */
	public static final int XK_hyphen = 0x00ad; /* U+00AD SOFT HYPHEN */
	public static final int XK_registered = 0x00ae; /* U+00AE REGISTERED SIGN */
	public static final int XK_macron = 0x00af; /* U+00AF MACRON */
	public static final int XK_degree = 0x00b0; /* U+00B0 DEGREE SIGN */
	public static final int XK_plusminus = 0x00b1; /* U+00B1 PLUS-MINUS SIGN */
	public static final int XK_twosuperior = 0x00b2; /*
														 * U+00B2 SUPERSCRIPT TWO
														 */
	public static final int XK_threesuperior = 0x00b3; /*
														 * U+00B3 SUPERSCRIPT THREE
														 */
	public static final int XK_acute = 0x00b4; /* U+00B4 ACUTE ACCENT */
	public static final int XK_mu = 0x00b5; /* U+00B5 MICRO SIGN */
	public static final int XK_paragraph = 0x00b6; /* U+00B6 PILCROW SIGN */
	public static final int XK_periodcentered = 0x00b7; /* U+00B7 MIDDLE DOT */
	public static final int XK_cedilla = 0x00b8; /* U+00B8 CEDILLA */
	public static final int XK_onesuperior = 0x00b9; /*
														 * U+00B9 SUPERSCRIPT ONE
														 */
	public static final int XK_masculine = 0x00ba; /*
													 * U+00BA MASCULINE ORDINAL INDICATOR
													 */
	public static final int XK_guillemotright = 0x00bb; /*
														 * U+00BB RIGHT-POINTING DOUBLE ANGLE QUOTATION MARK
														 */
	public static final int XK_onequarter = 0x00bc; /*
													 * U+00BC VULGAR FRACTION ONE QUARTER
													 */
	public static final int XK_onehalf = 0x00bd; /*
													 * U+00BD VULGAR FRACTION ONE HALF
													 */
	public static final int XK_threequarters = 0x00be; /*
														 * U+00BE VULGAR FRACTION THREE QUARTERS
														 */
	public static final int XK_questiondown = 0x00bf; /*
														 * U+00BF INVERTED QUESTION MARK
														 */
	public static final int XK_Agrave = 0x00c0; /*
												 * U+00C0 LATIN CAPITAL LETTER A WITH GRAVE
												 */
	public static final int XK_Aacute = 0x00c1; /*
												 * U+00C1 LATIN CAPITAL LETTER A WITH ACUTE
												 */
	public static final int XK_Acircumflex = 0x00c2; /*
														 * U+00C2 LATIN CAPITAL LETTER A WITH CIRCUMFLEX
														 */
	public static final int XK_Atilde = 0x00c3; /*
												 * U+00C3 LATIN CAPITAL LETTER A WITH TILDE
												 */
	public static final int XK_Adiaeresis = 0x00c4; /*
													 * U+00C4 LATIN CAPITAL LETTER A WITH DIAERESIS
													 */
	public static final int XK_Aring = 0x00c5; /*
												 * U+00C5 LATIN CAPITAL LETTER A WITH RING ABOVE
												 */
	public static final int XK_AE = 0x00c6; /* U+00C6 LATIN CAPITAL LETTER AE */
	public static final int XK_Ccedilla = 0x00c7; /*
													 * U+00C7 LATIN CAPITAL LETTER C WITH CEDILLA
													 */
	public static final int XK_Egrave = 0x00c8; /*
												 * U+00C8 LATIN CAPITAL LETTER E WITH GRAVE
												 */
	public static final int XK_Eacute = 0x00c9; /*
												 * U+00C9 LATIN CAPITAL LETTER E WITH ACUTE
												 */
	public static final int XK_Ecircumflex = 0x00ca; /*
														 * U+00CA LATIN CAPITAL LETTER E WITH CIRCUMFLEX
														 */
	public static final int XK_Ediaeresis = 0x00cb; /*
													 * U+00CB LATIN CAPITAL LETTER E WITH DIAERESIS
													 */
	public static final int XK_Igrave = 0x00cc; /*
												 * U+00CC LATIN CAPITAL LETTER I WITH GRAVE
												 */
	public static final int XK_Iacute = 0x00cd; /*
												 * U+00CD LATIN CAPITAL LETTER I WITH ACUTE
												 */
	public static final int XK_Icircumflex = 0x00ce; /*
														 * U+00CE LATIN CAPITAL LETTER I WITH CIRCUMFLEX
														 */
	public static final int XK_Idiaeresis = 0x00cf; /*
													 * U+00CF LATIN CAPITAL LETTER I WITH DIAERESIS
													 */
	public static final int XK_ETH = 0x00d0; /*
												 * U+00D0 LATIN CAPITAL LETTER ETH
												 */
	public static final int XK_Eth = 0x00d0; /* deprecated */
	public static final int XK_Ntilde = 0x00d1; /*
												 * U+00D1 LATIN CAPITAL LETTER N WITH TILDE
												 */
	public static final int XK_Ograve = 0x00d2; /*
												 * U+00D2 LATIN CAPITAL LETTER O WITH GRAVE
												 */
	public static final int XK_Oacute = 0x00d3; /*
												 * U+00D3 LATIN CAPITAL LETTER O WITH ACUTE
												 */
	public static final int XK_Ocircumflex = 0x00d4; /*
														 * U+00D4 LATIN CAPITAL LETTER O WITH CIRCUMFLEX
														 */
	public static final int XK_Otilde = 0x00d5; /*
												 * U+00D5 LATIN CAPITAL LETTER O WITH TILDE
												 */
	public static final int XK_Odiaeresis = 0x00d6; /*
													 * U+00D6 LATIN CAPITAL LETTER O WITH DIAERESIS
													 */
	public static final int XK_multiply = 0x00d7; /*
													 * U+00D7 MULTIPLICATION SIGN
													 */
	public static final int XK_Oslash = 0x00d8; /*
												 * U+00D8 LATIN CAPITAL LETTER O WITH STROKE
												 */
	public static final int XK_Ooblique = 0x00d8; /*
													 * U+00D8 LATIN CAPITAL LETTER O WITH STROKE
													 */
	public static final int XK_Ugrave = 0x00d9; /*
												 * U+00D9 LATIN CAPITAL LETTER U WITH GRAVE
												 */
	public static final int XK_Uacute = 0x00da; /*
												 * U+00DA LATIN CAPITAL LETTER U WITH ACUTE
												 */
	public static final int XK_Ucircumflex = 0x00db; /*
														 * U+00DB LATIN CAPITAL LETTER U WITH CIRCUMFLEX
														 */
	public static final int XK_Udiaeresis = 0x00dc; /*
													 * U+00DC LATIN CAPITAL LETTER U WITH DIAERESIS
													 */
	public static final int XK_Yacute = 0x00dd; /*
												 * U+00DD LATIN CAPITAL LETTER Y WITH ACUTE
												 */
	public static final int XK_THORN = 0x00de; /*
												 * U+00DE LATIN CAPITAL LETTER THORN
												 */
	public static final int XK_Thorn = 0x00de; /* deprecated */
	public static final int XK_ssharp = 0x00df; /*
												 * U+00DF LATIN SMALL LETTER SHARP S
												 */
	public static final int XK_agrave = 0x00e0; /*
												 * U+00E0 LATIN SMALL LETTER A WITH GRAVE
												 */
	public static final int XK_aacute = 0x00e1; /*
												 * U+00E1 LATIN SMALL LETTER A WITH ACUTE
												 */
	public static final int XK_acircumflex = 0x00e2; /*
														 * U+00E2 LATIN SMALL LETTER A WITH CIRCUMFLEX
														 */
	public static final int XK_atilde = 0x00e3; /*
												 * U+00E3 LATIN SMALL LETTER A WITH TILDE
												 */
	public static final int XK_adiaeresis = 0x00e4; /*
													 * U+00E4 LATIN SMALL LETTER A WITH DIAERESIS
													 */
	public static final int XK_aring = 0x00e5; /*
												 * U+00E5 LATIN SMALL LETTER A WITH RING ABOVE
												 */
	public static final int XK_ae = 0x00e6; /* U+00E6 LATIN SMALL LETTER AE */
	public static final int XK_ccedilla = 0x00e7; /*
													 * U+00E7 LATIN SMALL LETTER C WITH CEDILLA
													 */
	public static final int XK_egrave = 0x00e8; /*
												 * U+00E8 LATIN SMALL LETTER E WITH GRAVE
												 */
	public static final int XK_eacute = 0x00e9; /*
												 * U+00E9 LATIN SMALL LETTER E WITH ACUTE
												 */
	public static final int XK_ecircumflex = 0x00ea; /*
														 * U+00EA LATIN SMALL LETTER E WITH CIRCUMFLEX
														 */
	public static final int XK_ediaeresis = 0x00eb; /*
													 * U+00EB LATIN SMALL LETTER E WITH DIAERESIS
													 */
	public static final int XK_igrave = 0x00ec; /*
												 * U+00EC LATIN SMALL LETTER I WITH GRAVE
												 */
	public static final int XK_iacute = 0x00ed; /*
												 * U+00ED LATIN SMALL LETTER I WITH ACUTE
												 */
	public static final int XK_icircumflex = 0x00ee; /*
														 * U+00EE LATIN SMALL LETTER I WITH CIRCUMFLEX
														 */
	public static final int XK_idiaeresis = 0x00ef; /*
													 * U+00EF LATIN SMALL LETTER I WITH DIAERESIS
													 */
	public static final int XK_eth = 0x00f0; /* U+00F0 LATIN SMALL LETTER ETH */
	public static final int XK_ntilde = 0x00f1; /*
												 * U+00F1 LATIN SMALL LETTER N WITH TILDE
												 */
	public static final int XK_ograve = 0x00f2; /*
												 * U+00F2 LATIN SMALL LETTER O WITH GRAVE
												 */
	public static final int XK_oacute = 0x00f3; /*
												 * U+00F3 LATIN SMALL LETTER O WITH ACUTE
												 */
	public static final int XK_ocircumflex = 0x00f4; /*
														 * U+00F4 LATIN SMALL LETTER O WITH CIRCUMFLEX
														 */
	public static final int XK_otilde = 0x00f5; /*
												 * U+00F5 LATIN SMALL LETTER O WITH TILDE
												 */
	public static final int XK_odiaeresis = 0x00f6; /*
													 * U+00F6 LATIN SMALL LETTER O WITH DIAERESIS
													 */
	public static final int XK_division = 0x00f7; /* U+00F7 DIVISION SIGN */
	public static final int XK_oslash = 0x00f8; /*
												 * U+00F8 LATIN SMALL LETTER O WITH STROKE
												 */
	public static final int XK_ooblique = 0x00f8; /*
													 * U+00F8 LATIN SMALL LETTER O WITH STROKE
													 */
	public static final int XK_ugrave = 0x00f9; /*
												 * U+00F9 LATIN SMALL LETTER U WITH GRAVE
												 */
	public static final int XK_uacute = 0x00fa; /*
												 * U+00FA LATIN SMALL LETTER U WITH ACUTE
												 */
	public static final int XK_ucircumflex = 0x00fb; /*
														 * U+00FB LATIN SMALL LETTER U WITH CIRCUMFLEX
														 */
	public static final int XK_udiaeresis = 0x00fc; /*
													 * U+00FC LATIN SMALL LETTER U WITH DIAERESIS
													 */
	public static final int XK_yacute = 0x00fd; /*
												 * U+00FD LATIN SMALL LETTER Y WITH ACUTE
												 */
	public static final int XK_thorn = 0x00fe; /*
												 * U+00FE LATIN SMALL LETTER THORN
												 */
	public static final int XK_ydiaeresis = 0x00ff; /*
													 * U+00FF LATIN SMALL LETTER Y WITH DIAERESIS
													 */
	/* XK_LATIN1 */

}