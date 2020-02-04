import static java.time.temporal.TemporalAdjusters.next;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

public class AssignmentThree {
	public static void main(String[] args) {
		String[] array = { "1", "2", "3", "apple", "banana", "cherry", "dog", "egg", "episode", "75" };
		System.out.println("Original list: " + Arrays.asList(array));
		List<String> lengthList = Arrays.stream(array)
				.sorted((String s1, String s2) -> ((Integer) s1.length()).compareTo(((Integer) s2.length())))
				.collect(Collectors.toList());
		System.out.println("Sorted by shortest to longest: " + lengthList);
		List<String> reverseLengthList = Arrays.stream(array)
				.sorted((String s1, String s2) -> ((Integer) s2.length()).compareTo(((Integer) s1.length())))
				.collect(Collectors.toList());
		System.out.println("Sorted by longest to shortest: " + reverseLengthList);
		List<String> firstCharList = Arrays.stream(array)
				.sorted((String s1, String s2) -> ((Character) s1.charAt(0)).compareTo(((Character) s2.charAt(0))))
				.collect(Collectors.toList());
		System.out.println("Sorted by first character: " + firstCharList);
		List<String> eList = Arrays.stream(array).sorted((String s1, String s2) -> {
			if (s1.charAt(0) == 'e' && s2.charAt(0) == 'e') {
				return 0;
			} else if (s1.charAt(0) == 'e' && s2.charAt(0) != 'e') {
				return -1;
			} else if (s1.charAt(0) != 'e' && s2.charAt(0) == 'e') {
				return 1;
			} else {
				return s1.compareTo(s2);
			}
		}).collect(Collectors.toList());
		System.out.println("Sorted by letter e first: " + eList);
		Arrays.sort(array, (s1, s2) -> helper(s1, s2));
		System.out.println("Sorted by letter e first with helper: " + Arrays.asList(array));

		System.out.println();

		List<Integer> list = new ArrayList<Integer>();
		list.add(3);
		list.add(44);
		System.out.println("Input: " + list);
		String output = list.stream().map(i -> {
			if (i % 2 == 0) {
				return "e" + i;
			} else {
				return "o" + i;
			}
		}).reduce((i, j) -> {
			return i + "," + j;
		}).get();
		System.out.println("Output: " + output);

		System.out.println();

		List<String> list2 = Arrays.asList("abc", "abcd", "acb", "cab", "bac", "ABC");
		System.out.println("Original list: " + list2);
		List<String> newList = list2.stream().filter(i -> {
			return i.charAt(0) == 'a' && i.length() == 3;
		}).collect(Collectors.toList());
		System.out.println("New list (starts with a and 3 characters long): " + newList);

		System.out.println();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss:nnnnnnnnnZ");
		OffsetDateTime odt = OffsetDateTime.of(2019, 12, 12, 12, 12, 12, 123456789, ZoneOffset.UTC);
		System.out.println("Specific Date/Time nanosecond precision: " + odt.format(formatter));

		Random random = new Random();
		int minDay = (int) LocalDate.of(1900, 1, 1).toEpochDay();
		int maxDay = (int) LocalDate.of(2015, 1, 1).toEpochDay();
		long randomDay = minDay + random.nextInt(maxDay - minDay);
		LocalDate randomBirthDate = LocalDate.ofEpochDay(randomDay);
		System.out.print(
				"Random date: " + randomBirthDate + " / " + "One week ago: " + randomBirthDate.minusWeeks(1) + "\n");

		LocalDateTime now = LocalDateTime.now();
		ZoneId zone = ZoneId.of("Asia/Tokyo");
		ZoneOffset zoneOffSet = zone.getRules().getOffset(now);
		System.out.println("ZoneId: " + zone + " / " + "ZoneOffset: " + zoneOffSet);

		Instant instant = Instant.now();
		ZonedDateTime jpTime = instant.atZone(ZoneId.of("Asia/Tokyo"));
		System.out.println("Instant: " + instant + " / " + "ZonedDateTime: " + jpTime);
		System.out.println("ZonedDateTime: " + jpTime + " / " + "Instant: " + jpTime.toInstant());

		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter a year: ");
		int year = scanner.nextInt();
		System.out.print("Length of each month: ");
		for (int i = 1; i <= 12; i++) {
			System.out.print(YearMonth.of(year, i).lengthOfMonth() + " ");
		}

		System.out.print("\nEnter a month of the current year: ");
		int month = scanner.nextInt();
		for (LocalDate localDate = YearMonth.of(2019, month).atDay(1); localDate
				.getMonthValue() == month; localDate = localDate.plusWeeks(1)) {
			LocalDate monday = localDate.with(next(DayOfWeek.MONDAY));
			if (monday.getMonthValue() == month) {
				System.out.print(localDate.with(next(DayOfWeek.MONDAY)) + " ");
			}
		}

		System.out.println("\nEnter a date MM/dd/YYYY:");
		scanner.nextLine();
		String[] date = scanner.nextLine().split("/");
		int m = Integer.parseInt(date[0]);
		int d = Integer.parseInt(date[1]);
		int y = Integer.parseInt(date[2]);
		LocalDate ld = YearMonth.of(y, m).atDay(d);
		if (d == 13 && ld.getDayOfWeek().equals(DayOfWeek.FRIDAY)) {
			System.out.println(ld + " is Friday the 13th");
		} else {
			System.out.println(ld + " is not Friday the 13th");
		}
	}

	public static int helper(String s1, String s2) {
		if (s1.charAt(0) == 'e' && s2.charAt(0) == 'e') {
			return 0;
		} else if (s1.charAt(0) == 'e' && s2.charAt(0) != 'e') {
			return -1;
		} else if (s1.charAt(0) != 'e' && s2.charAt(0) == 'e') {
			return 1;
		} else {
			return s1.compareTo(s2);
		}
	}
}