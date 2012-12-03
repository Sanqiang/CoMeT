package Struct;

public class PostingIndex {

    public String Term;
    public String DocumentId;
    public int Frequency;
    public int StartIndex;
    public int DocumentLength;

    public PostingIndex(String Term, String DocumentId, int Frequency, int StartIndex, int DocumentLength) {
        this.Term = Term;
        this.DocumentId = DocumentId;
        this.Frequency = Frequency;
        this.StartIndex = StartIndex;
        this.DocumentLength = DocumentLength;
    }

    public PostingIndex() {
    }
}
