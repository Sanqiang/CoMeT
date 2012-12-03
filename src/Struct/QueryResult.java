package Struct;

public class QueryResult {
    public String DocumentId;
    public double Score;
    public String Word;
    public int Frequency;
    public int DocumentLength;

    public QueryResult(String DocumentId, String Word, int Frequency, int DocumentLength) {
        this.DocumentId = DocumentId;
        this.Word = Word;
        this.Frequency = Frequency;
        this.Score = 0;
        this.DocumentLength = DocumentLength;
    }
}
