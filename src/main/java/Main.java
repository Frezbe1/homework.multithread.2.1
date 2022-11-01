import java.util.*;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    private static String text;

    public static void main(String[] args) throws InterruptedException {
        String[] routes = new String[1000];
        List<Thread> threads = new ArrayList<>();

        for (String route : routes) {
            Runnable logic = () -> {
                text = generateRoute("RLRFR", 100);
                synchronized (sizeToFreq) {
                    int maxSize = 0;
                    for (int i = 0; i < text.length(); i++) {
                        if (text.charAt(i) == 'R') maxSize++;
                    }
                    if (!sizeToFreq.containsKey(maxSize)) {
                        sizeToFreq.put(maxSize, 1);
                    } else {
                        int newNum = sizeToFreq.get(maxSize) + 1;
                        sizeToFreq.put(maxSize, newNum);
                    }
                }
            };

            Thread thread = new Thread(logic);
            threads.add(thread);
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join(); // зависаем, ждём когда поток объект которого лежит в thread завершится
        }
        Set<Map.Entry<Integer, Integer>> entries = sizeToFreq.entrySet();
        Integer valueBig = 0;
        for (Map.Entry<Integer, Integer> pair : entries) {
            Integer value = pair.getValue();
            if (value > valueBig) {
                valueBig = value;
            }
        }
        System.out.println("Самое частое количество повторений " + getKeyByValue(sizeToFreq, valueBig)
                + " (встретилось " + valueBig + " раз)");
        System.out.println("Другие размеры:");
        for (Map.Entry<Integer, Integer> pair : entries) {
            Integer key = pair.getKey();
            Integer value = pair.getValue();
            System.out.println("- " + key + " (" + value + " раз) ");
        }
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
}
