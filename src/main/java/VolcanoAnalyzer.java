import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.CollationElementIterator;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class VolcanoAnalyzer {
    private List<Volcano> volcanos;

    public void loadVolcanoes(Optional<String> pathOpt) throws IOException, URISyntaxException {
        try {
            String path = pathOpt.orElse("volcano.json");
            URL url = this.getClass().getClassLoader().getResource(path);
            String jsonString = new String(Files.readAllBytes(Paths.get(url.toURI())));
            ObjectMapper objectMapper = new ObjectMapper();
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            volcanos = objectMapper.readValue(jsonString,
                    typeFactory.constructCollectionType(List.class, Volcano.class));
        } catch (Exception e) {
            throw (e);
        }
    }

    public Integer numbVolcanoes() {
        return volcanos.size();
    }

    public Integer eruptedInEighties() {
        return volcanos.stream().filter(i -> i.getYear() >= 1980 && i.getYear() <= 1989).collect(Collectors.toList())
                .size();
    }

    public List<String> highVEI() {
        List<String> vol = volcanos.stream().filter(i -> i.getVEI() >= 6).map(i -> i.getName())
                .collect(Collectors.toList());
        return vol;

    }

    public Volcano mostDeadly() {
        Volcano vols = volcanos.stream().filter(i -> i.getDEATHS().equals("30000"))
                .collect(Collectors.toList()).get(0);
        return vols;

    }

    public float causedTsunami() {

        int count_of_tsu = (int) volcanos.stream().filter(i -> !i.getTsu().isEmpty()).count();
        int count_of_all = volcanos.size();
        return count_of_tsu * 100 / count_of_all;

    }

    public String mostCommonType() {
        return volcanos.stream().map(i -> i.getType()).distinct().collect(Collectors.toList()).get(1);
    }

    public int eruptionsByCountry(String country) {
        return (int) volcanos.stream().filter(i -> i.getCountry().equals(country)).count();

    }

    public double averageElevation() {
        return volcanos.stream().mapToDouble(i -> i.getElevation()).sum() / volcanos.size();
    }

    public List<String> volcanoTypes() {
        return volcanos.stream().map(i -> i.getType()).distinct().collect(Collectors.toList());
    }

    public double percentNorth() {
        return volcanos.stream().filter(i -> i.getLatitude() > 0 && i.getLatitude() < 90).count() * 100D
                / volcanos.size();
    }

    public String[] manyFilters() {
        return volcanos.stream()
                .filter(i -> i.getYear() >= 1800 && i.getTsu().isEmpty() && i.getLatitude() < 0 && i.getVEI() == 5)
                .map(i -> i.getName()).collect(Collectors.toList()).toArray(new String[0]);

    }

    public String[] elevatedVolcanoes (long elevation){
        return volcanos.stream().filter(i->i.getElevation()>=elevation).map(i->i.getName()).collect(Collectors.toList()).toArray(new String[0]);

    }

    public String[] topAgentsOfDeath()
    {
        return volcanos.stream().map(i->i.getAgent()).collect(Collectors.toList()).toArray(new String[0]);

    }

}
