package fr._42.numbers;

import fr._42.numbers.IllegalNumberException;

public class NumberWorker {
    public boolean isPrime(int number) {
        if (number <= 1) {
            throw new IllegalNumberException();
        } if (number <= 3) {
            return true;
        } if (number % 2 == 0 || number % 3 == 0) {
            return false;
        }

        int i = 5;
        while (true) {
            if (i * i > number) {
                break;
            } if (number % i == 0 || number % (i + 2) == 0) {
                return false;
            }

            i += 6;
        }

        return true;
    }

    public int digitsSum(int number) {
        if (number == 0) {
            return 0;
        }

        int nbr = number % 10;
        if (nbr < 0) {
            nbr = -nbr;
        }

        return nbr + digitsSum(number / 10);
    }
}
