package ttl.larku.jconfig;

import java.util.regex.Pattern;

import org.springframework.core.type.filter.RegexPatternTypeFilter;

class ControllerFilter extends RegexPatternTypeFilter
{
    public ControllerFilter() {
        super(Pattern.compile("ttl\\.larku\\.controllers\\..*"));
    }
}
