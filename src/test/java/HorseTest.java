import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

public class HorseTest {
    @Test
    public void nameIsNullException() {
        assertThrows(IllegalArgumentException.class, () -> new Horse(null, 1, 1));
    }

    @Test
    public void nameIsNullMessage() {
        try {
            new Horse(null, 1, 1);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Name cannot be null.", e.getMessage());
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   ", "\t", "\n"})
    public void nameIsBlankException(String name) {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new Horse(name, 1, 1));
        assertEquals("Name cannot be blank.", e.getMessage());
    }

    @Test
    public void speedIsPositiveException() {
        try {
            new Horse("Pegasus", -1, 1);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Speed cannot be negative.", e.getMessage());
        }
    }

    @Test
    public void distanceIsPositiveException() {
        try {
            new Horse("Pegasus", 1, -1);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Distance cannot be negative.", e.getMessage());
        }
    }

    @Test
    public void getName() throws NoSuchFieldException, IllegalAccessException {
        String expectedHorseName = "Pegasus";
        Horse horse = new Horse(expectedHorseName, 1, 1);

        Field name = Horse.class.getDeclaredField("name");
        name.setAccessible(true);
        String nameValue = (String) name.get(horse);
        assertEquals(expectedHorseName, nameValue);
    }

    @Test
    public void getSpeed() throws NoSuchFieldException, IllegalAccessException {
        Double expectedSpeed = 2.5;
        Horse horse = new Horse("Cherry", expectedSpeed, 1);

        Field speed = Horse.class.getDeclaredField("speed");
        speed.setAccessible(true);
        Double speedValue = (Double) speed.get(horse);
        assertEquals(expectedSpeed, speedValue);
    }

    @Test
    public void getDistance() throws NoSuchFieldException, IllegalAccessException {
        Double expectedDistance = 10.5;
        Horse horse = new Horse("Cherry", 2, expectedDistance);

        Field distance = Horse.class.getDeclaredField("distance");
        distance.setAccessible(true);
        Double distanceValue = (Double) distance.get(horse);
        assertEquals(expectedDistance, distanceValue);
    }

    @Test
    public void getDistanceByDefault() throws NoSuchFieldException, IllegalAccessException {
        Horse horse = new Horse("Cherry", 2);

        Field distance = Horse.class.getDeclaredField("distance");
        distance.setAccessible(true);
        Double distanceValue = (Double) distance.get(horse);
        assertEquals(0, distanceValue);
    }

    @Test
    public void moveUsesGetRandomDouble() {
        try (MockedStatic<Horse> mockedStatic = mockStatic(Horse.class)) {
            new Horse("Spark", 2.5, 120).move();

            mockedStatic.verify(() -> Horse.getRandomDouble(0.2, 0.9));
        }
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.1, 0.2, 0.5, 0.9, 1.0, 999.999, 0.0})
    public void move(double random) {
        try (MockedStatic<Horse> mockedStatic = mockStatic(Horse.class)) {
            Horse horse = new Horse("Spark", 2.7, 45);
            mockedStatic.when(() -> Horse.getRandomDouble(0.2, 0.9)).thenReturn(random);

            horse.move();

            assertEquals(45 + 2.7 * random, horse.getDistance());
        }
    }

}
