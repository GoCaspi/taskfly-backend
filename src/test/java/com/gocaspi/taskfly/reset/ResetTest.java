package com.gocaspi.taskfly.reset;


import com.google.common.hash.Hashing;
import org.junit.jupiter.api.Test;


import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

 class ResetTest {
	Reset reset = new Reset("abc","abc@mail.com");

	class Testcase_setString{
		final String newText;

		Testcase_setString(String newText) {
			this.newText = newText;
		}
	}

	Testcase_setString[] testcases = new Testcase_setString[]{
			new Testcase_setString("abc"),
			new Testcase_setString(null),
			new Testcase_setString(""),
	};

	class Testcase_getString{
		final String expected;

		Testcase_getString(String expected) {
			this.expected = expected;
		}
	}

	Testcase_getString[] testcases_get = new Testcase_getString[]{
			new Testcase_getString("abc"),
			new Testcase_getString(null),
			new Testcase_getString(""),
	};


	@Test
	 void getLastName() {
		Reset r = reset; // TODO Replace default value.
		for (Testcase_getString tc : testcases_get){
			r.setLastName(tc.expected);
			String actual = r.getLastName();
			assertEquals(actual,tc.expected);
		}
	}

	@Test
	 void getEmail() {
		Reset r = reset; // TODO Replace default value.
		for (Testcase_getString tc : testcases_get){
			r.setEmail(tc.expected);
			String actual = r.getEmail();
			assertEquals(actual,tc.expected);
		}
	}
	@Test
	 void setEmail() {
		Reset r = reset; // TODO Replace default value.
		for (Testcase_getString tc : testcases_get){
			r.setEmail(tc.expected);
			assertEquals(r.getEmail(),tc.expected);
		}
	}

	@Test
	 void setLastName() {
		Reset r = reset; // TODO Replace default value.
		for (Testcase_getString tc : testcases_get){
			r.setLastName(tc.expected);
			assertEquals(r.getLastName(),tc.expected);
		}
	}

	@Test
	 void hashStr(){
		Reset r = reset;
		String expected = Hashing.sha256()
				.hashString(r.getEmail(), StandardCharsets.UTF_8)
				.toString();
		String actual = hashStr(r.getEmail());
		assertEquals(expected,actual);
	}
	public String hashStr(String str) {
		String sha256hex = Hashing.sha256()
				.hashString(str, StandardCharsets.UTF_8)
				.toString();

		return  sha256hex;

	}
}
