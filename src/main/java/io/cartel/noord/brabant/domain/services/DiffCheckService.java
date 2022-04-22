package io.cartel.noord.brabant.domain.services;

import io.cartel.noord.brabant.api.dtos.DiffResponse;
import io.cartel.noord.brabant.api.dtos.Difference;
import io.cartel.noord.brabant.api.enums.DiffResult;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;

/**
 * {@link DiffService} abstracts the diff algorithm.
 */
@Service
public class DiffCheckService {

    /**
     * Compares two strings and returns differences.
     *
     * @param left  left side comparison data
     * @param right right side comparison data
     * @return diff results
     */
    public DiffResponse getDiff(@NotNull String left, @NotNull String right) {

        if (left.length() != right.length()) {
            return new DiffResponse(DiffResult.DIFFERENT_SIZES, emptyList());
        }

        if (left.equals(right)) {
            return new DiffResponse(DiffResult.EQUAL, emptyList());
        }

        return new DiffResponse(DiffResult.DIFFERENT, calculateDiff(left, right));
    }

    /**
     * Linearly compares two strings (char-by-char) and accumulate offsets of each diff occurrence
     */
    private List<Difference> calculateDiff(String left, String right) {
        var differences = new ArrayList<Difference>();
        var diffOffset = 0L;
        var accumulating = false;

        for (int offset = 0; offset < left.length(); offset++) {

            var isDifferent = left.charAt(offset) != right.charAt(offset);

            //Begins a diff
            if (isDifferent && !accumulating) {
                diffOffset = offset;
                accumulating = true;
                continue;
            }

            //Ends a diff
            if (!isDifferent && accumulating) {
                differences.add(new Difference(diffOffset, offset - diffOffset));
                accumulating = false;
            }
        }

        //When it is still accumulating means that the rest belongs to this diff
        if (accumulating) {
            differences.add(new Difference(diffOffset, left.length() - diffOffset));
        }

        return differences;
    }
}
