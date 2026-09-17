import{describe,expect,it}from'vitest';import{makeTheme}from'./theme';
describe('kurumsal tema',()=>{it('aydınlık ve karanlık temayı üretir',()=>{expect(makeTheme('light').palette.mode).toBe('light');expect(makeTheme('dark').palette.mode).toBe('dark')})});

