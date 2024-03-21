import groovy.lang.GString;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class checkStringValue {
    @ParameterizedTest()
    @ValueSource(strings = {"someTextHereAndThere","someTextAnd","some"})
    public void checkVariableValue(String textForCheck){
     assertFalse(textForCheck.length() > 15 ,"Text value is more than 15 characters");
    }
}
