package hamdan.JuniorDesign.DigitalNumPlateDetector;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Objects;

public class SeparateSentencesActivity extends AppCompatActivity {

    TextView mainText, separatedText;
    StringBuilder message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_separate_sentences);

        mainText = findViewById(R.id.mainTopic);
        separatedText = findViewById(R.id.separatedSentenceText);

        String topic = Objects.requireNonNull(getIntent().getExtras()).getString("topic");

        mainText.setText(topic);

        separateSentences(topic);

    }

    private void separateSentences(String topic) {
        String[] sentences = topic.split("(?<=[.!?;])\\s");
        message = new StringBuilder("");
        int i = 1;

        for (String sen : sentences) {
            message.append(i).append(". ").append(sen).append("\n");
            i++;
        }

        separatedText.setText(message);
    }
}
