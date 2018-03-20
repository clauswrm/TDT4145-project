package databaser.persistence;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;


/**
 * REALLY not how integration tests are supposed to be used, but since this is a small project,
 * without test requirements and I wanted to only create one test class.
 * <p>
 * WARNING:
 * <p>
 * Requires a valid database connection as specified in {@link ActiveDomainObject} to succeed.
 * Alters the live database.
 *
 * @see ActiveDomainObject#dbURL
 * @see ActiveDomainObject#dbUsername
 * @see ActiveDomainObject#dbPassword
 */
public class PersistenceTest {

    private Apparat romaskin;
    private Apparatøvelse roing;
    private Friøvelse pushups;
    private Treningsøkt økt;
    private Notat notat;
    private Øvelsesgruppe armøvelser;

    @Before
    public void setUp() {
        romaskin = new Apparat("Romaskin", "Du ror til du dør");
        roing = new Apparatøvelse("Roing", "Ro ro ro din båt", romaskin);
        pushups = new Friøvelse("Pushups", "Dytt opp og ned");
        økt = new Treningsøkt(new GregorianCalendar(2018, Calendar.JANUARY, 1).getTime(), 45, 6, 8);
        notat = new Notat("Dette var en veldig bra økt der jeg kjente smerte!", økt);
        armøvelser = new Øvelsesgruppe("Armøvelser");

        romaskin.save();
        roing.save();
        pushups.save();
        økt.save();
        notat.save();
        armøvelser.save();
    }

    @Test
    public void shouldAddØvelserToØvelsesgruppe() {
        // Given
        roing.addToØvelsesgruppe(armøvelser);
        pushups.addToØvelsesgruppe(armøvelser);

        // When
        final List<Øvelse> øvelser = armøvelser.getØvelser();

        // Then
        assertThat(øvelser.size(), is(2));
        assertThat(øvelser, hasItems(roing, pushups));
    }

    @Test
    public void shouldAddØvelserToTreningsøkt() {
        // Given
        økt.addApparatøvelse(roing, 4, 12, 4);
        økt.addFriøvelse(pushups, "Max push x 4 med 1 min pause");

        // When
        final List<Øvelse> øvelserInTreningsøkt = økt.getØvelser();
        final List<Treningsøkt> treningsøkterWithRoing = roing.getTreningsøkterWithØvelse();
        final List<Treningsøkt> treningsøkterWithPushups = pushups.getTreningsøkterWithØvelse();

        final Map<Treningsøkt, Map<String, Integer>> progressForApparatøvelse = roing.getProgressForApparatøvelse();
        final Map<Treningsøkt, String> progressForFriøvelse = pushups.getProgressForFriøvelse();

        // Then
        assertThat(øvelserInTreningsøkt, hasSize(2));
        assertThat(øvelserInTreningsøkt, hasItems(roing, pushups));

        assertThat(treningsøkterWithRoing, hasSize(1));
        assertThat(treningsøkterWithRoing, hasItem(økt));

        assertThat(treningsøkterWithPushups, hasSize(1));
        assertThat(treningsøkterWithPushups, hasItem(økt));

        assertThat(progressForApparatøvelse.size(), is(1));
        assertThat(progressForApparatøvelse.get(økt).keySet(), hasItems("kilo", "reps", "set"));
        assertThat(progressForFriøvelse.size(), is(1));
    }

    @Test
    public void shouldGetNotaterForTreningsøkt() {
        // When
        final List<Notat> notater = økt.getNotater();

        // Then
        assertThat(notater, hasSize(1));
        assertThat(notater, hasItem(notat));
    }
}