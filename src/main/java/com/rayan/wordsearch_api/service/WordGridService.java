package com.rayan.wordsearch_api.service;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@ApplicationScoped
public class WordGridService {


    private enum Direction {
        HORIZONTAL, VERTICAL, DIAGONAL, HORIZONTAL_INVERSE, VERTICAL_INVERSE, DIAGONAL_INVERSE
    }

    private class Coordinate {
        int x;
        int y;

        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }


    public char[][] generateGrid(int gridSize, List<String> words) {

        List<Coordinate> coordinates = new ArrayList<Coordinate>();

        char[][] contents = new char[gridSize][gridSize];

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                coordinates.add(new Coordinate(i, j));
                contents[i][j] = '_';
            }
        }

        for (String word : words) {
            Collections.shuffle(coordinates);
            for (Coordinate coordenate : coordinates) {
                int x = coordenate.x;
                int y = coordenate.y;
                Direction selectedDirection = getDirectionForFit(contents, word, coordenate);
                if (selectedDirection != null) {
                    switch (selectedDirection) {
                        case HORIZONTAL:
                            for (char c : word.toCharArray()) {
                                contents[x][y++] = c;
                            }
                            break;
                        case VERTICAL:
                            for (char c : word.toCharArray()) {
                                contents[x++][y] = c;
                            }
                            break;
                        case DIAGONAL:
                            for (char c : word.toCharArray()) {
                                contents[x++][y++] = c;
                            }
                            break;
                        case HORIZONTAL_INVERSE:
                            for (char c : word.toCharArray()) {
                                contents[x][y--] = c;
                            }
                            break;
                        case VERTICAL_INVERSE:
                            for (char c : word.toCharArray()) {
                                contents[x--][y] = c;
                            }
                            break;
                        case DIAGONAL_INVERSE:
                            for (char c : word.toCharArray()) {
                                contents[x--][y--] = c;
                            }
                            break;

                    }
                    break;
                }
            }
        }
        randomFillGrid(contents);
        return contents;
    }

    public void displayGrid(char[][] contents) {
        int gridSize = contents[0].length;
        // Print the populated grid
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                System.out.print(contents[i][j] + " ");
            }
            System.out.println();
        }
    }


    private void randomFillGrid(char[][] contents) {
        int gridSize = contents[0].length;
        String allCapLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        char[] arabicLetters = {'أ', 'ب', 'ت', 'ث', 'ج', 'ح', 'خ', 'د', 'ذ', 'ر', 'ز', 'س', 'ش', 'ص',
                'ض', 'ط', 'ظ', 'ع', 'غ', 'ف', 'ق', 'ك', 'ل', 'م', 'ن', 'ه', 'و', 'ي'};

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (contents[i][j] == '_') {
                    int randomIndex = ThreadLocalRandom.current().nextInt(0, arabicLetters.length);
                    contents[i][j] = arabicLetters[randomIndex];
                }
            }
        }
    }

    private Direction getDirectionForFit(char[][] contents, String word, Coordinate coordinate) {
        List<Direction> directions = Arrays.asList(Direction.values());
        Collections.shuffle(directions);
        for (Direction direction : directions) {
            if (doesFit(contents, word, coordinate, direction)) {
                return direction;
            }
        }
        return null;
    }


    private boolean doesFit(char[][] contents, String word, Coordinate coordinate, Direction direction) {
        int gridSize = contents[0].length;
        int wordLength = word.length();
        switch (direction) {
            case HORIZONTAL:
                if (coordinate.y + wordLength > gridSize) return false;
                for (int i = 0; i < wordLength; i++) {
                    char letter = contents[coordinate.x][coordinate.y + i];
                    if (letter != '_' && letter != word.charAt(i)) return false;
                }
                break;
            case VERTICAL:
                if (coordinate.x + wordLength > gridSize) return false;
                for (int i = 0; i < wordLength; i++) {
                    char letter = contents[coordinate.x + i][coordinate.y];
                    if (letter != '_' && letter != word.charAt(i)) return false;
                }
                break;
            case DIAGONAL:
                if (coordinate.x + wordLength > gridSize || coordinate.y + wordLength > gridSize) return false;
                for (int i = 0; i < wordLength; i++) {
                    char letter = contents[coordinate.x + i][coordinate.y + i];
                    if (letter != '_' && letter != word.charAt(i)) return false;
                }
                break;
            case HORIZONTAL_INVERSE:
                if (coordinate.y < wordLength) return false;
                for (int i = 0; i < wordLength; i++) {
                    char letter = contents[coordinate.x][coordinate.y - i];
                    if (letter != '_' && letter != word.charAt(i)) return false;
                }
                break;
            case VERTICAL_INVERSE:
                if (coordinate.x < wordLength) return false;
                for (int i = 0; i < wordLength; i++) {
                    char letter = contents[coordinate.x - i][coordinate.y];
                    if (letter != '_' && letter != word.charAt(i)) return false;
                }
                break;
            case DIAGONAL_INVERSE:
                if (coordinate.x < wordLength || coordinate.y < wordLength) return false;
                for (int i = 0; i < wordLength; i++) {
                    char letter = contents[coordinate.x - i][coordinate.y - i];
                    if (letter != '_' && letter != word.charAt(i)) return false;
                }
                break;
        }
        return true;
    }
}
