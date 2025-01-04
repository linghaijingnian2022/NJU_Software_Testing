package mutation;

import seedsort.Seed;

public interface Mutator {
    Seed mutate(Seed seed);
}
