package xyz.ersut.core.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 王二飞
 */
public class BaseService<T extends BaseMapper<M>,M> extends ServiceImpl<T,M> implements IService<M> {


}
