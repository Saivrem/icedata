package in.ua.icetools.icedata.processors;

import java.util.List;

public interface Processor<T> {

    List<T> process() throws Exception;
}
