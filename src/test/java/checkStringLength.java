import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class checkStringLength {
    @ParameterizedTest()
    @ValueSource(strings = {"someTextHereAndThere","someTextAnd","some"})
    public void checkVariableLength(String textForCheck){
     assertFalse(textForCheck.length() > 15 ,"Text value is more than 15 characters");
    }
}
