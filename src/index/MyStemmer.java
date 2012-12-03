package index;

public class MyStemmer {

    int StartIndex;
    int EndIndex;
    String OrigWord;

    public MyStemmer(String orig) {
        OrigWord = orig.toLowerCase();
        StartIndex = 0;
        EndIndex = orig.length() - 1;
    }

    private void step1() {
        if (isEndWith("s", false)) {
            if (isEndWith("sses", false)) {
                EndIndex -= 2;
                //OrigWord = OrigWord.replaceAll("sses$", "ss");
            } else if (isEndWith("ies", false)) {
                EndIndex -= 2;
                //OrigWord = OrigWord.replaceAll("ies$", "ie"); 
            } else if (OrigWord.substring(OrigWord.length() - 1, OrigWord.length()).equals("s")) {
                EndIndex--;
                //OrigWord = OrigWord.replaceAll("s$", "");
            }
        }
        if (isEndWith("eed", false)) {
            if (getNumOfSyllable(OrigWord.length() - 1) > 0) {
                EndIndex--;
                //OrigWord = OrigWord.replaceAll("eed$", "ee");
            }
        } else if ((isEndWith("ed", true) || isEndWith("ing", true)) && isVowelInside()) {
            if (isEndWith("at", false)) {
                OrigWord = OrigWord.replaceAll("at$", "ate");
                EndIndex++;
            } else if (isEndWith("bl", false)) {
                OrigWord = OrigWord.replaceAll("at$", "ate");
                EndIndex++;
            } else if (isEndWith("iz", false)) {
                OrigWord = OrigWord.replaceAll("iz$", "ize");
                EndIndex++;
            } else if (isDoubleConsonant(EndIndex)) {
                EndIndex--;
                if (OrigWord.charAt(EndIndex) == 'l' || OrigWord.charAt(EndIndex) == 's' || OrigWord.charAt(EndIndex) == 'z') {
                    EndIndex++;
                }
            } else if (getNumOfSyllable(EndIndex) == 1 && isCVC(EndIndex)) {
                OrigWord += "e";
                EndIndex++;
            }
        }
    }

    private void step2() {
        if (isEndWith("y", false) && isVowelInside()) {
            OrigWord = OrigWord.replaceAll("y$", "i");
        }
    }

    private void step3() {
        if (EndIndex == 0) {
            return;
        }
        switch (OrigWord.charAt(EndIndex - 1)) {
            case 'a':
                if (isEndWith("ational", true)) {
                    OrigWord = OrigWord.replaceAll("ational$", "ate    ");
                    EndIndex += 4;
                }
                if (isEndWith("tional", true)) {
                    OrigWord = OrigWord.replaceAll("tional", "tion  ");
                    EndIndex += 2;
                }
                break;
            case 'c':
                if (isEndWith("izer", true)) {
                    OrigWord = OrigWord.replaceAll("enci", "ence");
                }
                if (isEndWith("anci", true)) {
                    OrigWord = OrigWord.replaceAll("anci", "ance");
                }
                break;
            case 'e':
                if (isEndWith("izer", true)) {
                    OrigWord = OrigWord.replaceAll("izer", "ize");
                }
                break;
            case 'l':
                if (isEndWith("bli", true)) {
                    OrigWord = OrigWord.replaceAll("bli", "ble");
                }
                if (isEndWith("alli", true)) {
                    OrigWord = OrigWord.replaceAll("alli", "al  ");
                    EndIndex += 2;
                }
                if (isEndWith("entli", true)) {
                    OrigWord = OrigWord.replaceAll("entli", "ent  ");
                    EndIndex += 2;
                }
                if (isEndWith("eli", true)) {
                    OrigWord = OrigWord.replaceAll("eli", "  e");
                    EndIndex += 2;
                }
                if (isEndWith("ousli", true)) {
                    OrigWord = OrigWord.replaceAll("ousli", "ous  ");
                    EndIndex += 2;
                }
                break;
            case 'o':
                if (isEndWith("ization", true)) {
                    OrigWord = OrigWord.replaceAll("ization", "ize    ");
                    EndIndex += 4;
                }
                if (isEndWith("ation", true)) {
                    OrigWord = OrigWord.replaceAll("ation", "ate  ");
                    EndIndex += 2;
                }
                if (isEndWith("ator", true)) {
                    OrigWord = OrigWord.replaceAll("ator", "ate ");
                    EndIndex++;
                }
                break;
            case 's':
                if (isEndWith("alism", true)) {
                    OrigWord = OrigWord.replaceAll("alism", "al   ");
                    EndIndex += 3;
                }
                if (isEndWith("iveness", true)) {
                    OrigWord = OrigWord.replaceAll("iveness", "ive    ");
                    EndIndex += 4;
                }
                if (isEndWith("fulness", true)) {
                    OrigWord = OrigWord.replaceAll("fulness", "ful    ");
                    EndIndex += 4;
                }
                if (isEndWith("ousness", true)) {
                    OrigWord = OrigWord.replaceAll("ousness", "ous    ");
                    EndIndex += 4;
                }
                break;
            case 't':
                if (isEndWith("aliti", true)) {
                    OrigWord = OrigWord.replaceAll("aliti", "al   ");
                    EndIndex += 3;
                }
                if (isEndWith("iviti", true)) {
                    OrigWord = OrigWord.replaceAll("iviti", "ive  ");
                    EndIndex += 2;
                }
                if (isEndWith("biliti", true)) {
                    OrigWord = OrigWord.replaceAll("biliti", "ble   ");
                    EndIndex += 3;
                }
                break;
            case 'g':
                if (isEndWith("logi", true)) {
                    OrigWord = OrigWord.replaceAll("logi", "log ");
                    EndIndex++;
                }
                break;
            default:
                break;
        }
    }

    private void step4() {
        switch (OrigWord.charAt(EndIndex)) {
            case 'e':
                if (isEndWith("icate", false)) {
                    OrigWord = OrigWord.replaceAll("icate", "ic   ");
                    EndIndex += 3;
                }
                if (isEndWith("ative", false)) {
                    OrigWord = OrigWord.replaceAll("ative", "     ");
                    EndIndex += 5;
                }
                if (isEndWith("alize", false)) {
                    OrigWord = OrigWord.replaceAll("alize", "al   ");
                    EndIndex += 3;
                }
                break;
            case 'i':
                if (isEndWith("iciti", false)) {
                    OrigWord = OrigWord.replaceAll("iciti", "ic   ");
                    EndIndex += 3;
                }
                break;
            case 'l':
                if (isEndWith("ical", false)) {
                    OrigWord = OrigWord.replaceAll("ical", "ic  ");
                    EndIndex += 2;
                }
                if (isEndWith("ful", false)) {
                    OrigWord = OrigWord.replaceAll("ful", "   ");
                    EndIndex += 3;
                }
                break;
            case 's':

                if (isEndWith("ness", false)) {
                    OrigWord = OrigWord.replaceAll("ness",     "");
                    EndIndex += 4;
                }
                break;
            default:
                break;
        }
    }

    private void step5() {
        if (EndIndex == 0) {
            return;
        }
        switch (OrigWord.charAt(EndIndex - 1)) {
            case 'a':
                isEndWith("al", true);
                break;
            case 'c':
                isEndWith("ance", true);
                isEndWith("ence", true);
                break;
            case 'e':
                isEndWith("er", true);
                break;
            case 'i':
                isEndWith("ic", true);
                break;
            case 'l':
                isEndWith("able", true);
                isEndWith("ible", true);
                break;
            case 'n':
                isEndWith("ant", true);
                isEndWith("ement", true);
                isEndWith("ment", true);
                isEndWith("ent", true);
                break;
            case 'o':
                if (isEndWith("sion", true) || isEndWith("tion", true)) {
                    EndIndex++;
                }
                isEndWith("ou", true);
                break;
            case 's':
                isEndWith("ism", true);
                break;
            case 't':
                isEndWith("ate", true);
                isEndWith("iti", true);
                break;
            case 'u':
                isEndWith("ous", true);
                break;
            case 'v':
                isEndWith("ive", true);
                break;
            case 'z':
                isEndWith("ize", true);
                break;
            default:
                break;
        }
    }

    private void step6() {
        if (OrigWord.charAt(EndIndex) == 'e') {
            int NumVC = getNumOfSyllable(EndIndex);
            if (NumVC > 0 || (NumVC == 1 && isCVC(EndIndex - 1))) {
                EndIndex--;
            }
        }
        if (OrigWord.charAt(EndIndex) == 'l' && isDoubleConsonant(EndIndex) && getNumOfSyllable(EndIndex) > 0) {
            EndIndex--;
        }
    }

    public void stem() {
        step1();
        step2();
        step3();
        step4();
        step5();
        step6();
    }

    //tools
    private boolean isConsonant(int Index) {
        char c = OrigWord.charAt(Index);
        switch (c) {
            case 'a':
            case 'e':
            case 'i':
            case 'o':
            case 'u':
                return false;
            case 'y':
                return (Index == 0) ? true : !isConsonant(Index - 1);
            default:
                return true;
        }
    }

    private boolean isVowelInside() {
        for (int i = 0; i < OrigWord.length(); i++) {
            if (!isConsonant(i)) {
                return true;
            }
        }
        return false;
    }

    private int getNumOfSyllable(int JEndIndex) {
        int CurrentIndex = 0;
        int NumVC = 0;
        while (true) {
            if (JEndIndex <= CurrentIndex) {
                return NumVC;
            }
            if (!isConsonant(CurrentIndex)) {
                break;
            }
            CurrentIndex++;
        }
        CurrentIndex++;
        while (true) {
            while (true) {
                if (JEndIndex <= CurrentIndex) {
                    return NumVC;
                }
                if (isConsonant(CurrentIndex)) {
                    break;
                }
                CurrentIndex++;
            }
            CurrentIndex++;
            NumVC++;
            while (true) {
                if (JEndIndex <= CurrentIndex) {
                    return NumVC;
                }
                if (!isConsonant(CurrentIndex)) {
                    break;
                }
                CurrentIndex++;
            }
            CurrentIndex++;
        }
    }

    private boolean isDoubleConsonant(int Index) {
        if (Index < 1) {
            return false;
        }
        if (OrigWord.charAt(Index) == OrigWord.charAt(Index - 1)) {
            return isConsonant(Index);
        } else {
            return false;
        }
    }

    private boolean isEndWith(String End, boolean MoveEndIndex) {
        //System.out.println(EndIndex +"/"+OrigWord.length());
        if (OrigWord.substring(0, EndIndex + 1).endsWith(End)) {
            if (MoveEndIndex) {
                EndIndex -= End.length();
            }
            return true;
        }
        return false;

    }

    private boolean isCVC(int Index) {
        if (Index < 2 || !isConsonant(Index) || isConsonant(Index - 1) || !isConsonant(Index - 2)) {
            return false;
        } else {
            int ch = OrigWord.charAt(Index);
            if (ch == 'w' || ch == 'x' || ch == 'y') {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return OrigWord.substring(0, EndIndex + 1);

    }
    public static String getResult(String Word) {
        MyStemmer x = new MyStemmer(Word);
        x.stem();
        return x.toString();
    }
}
