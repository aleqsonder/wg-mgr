import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest';
import {
    isUserContactResponse,
    isUserResponse,
    isUserResponseArray,
    isResponseMessage,
    fetchUsers,
    fetchUserById,
    createUser,
    deleteUser,
    editUser,
} from './usersApi';
import type { UserRequest } from '../types/userRelated';

// ─────────────────────────────────────────────────────────────────────────────
// Вспомогательные фикстуры
// ─────────────────────────────────────────────────────────────────────────────
const validContact = { contactType: 'telegram', content: '@alice' };
const validUser = { id: 1, username: 'alice', contacts: [validContact] };
const validUserArray = [validUser];
const errorMessage = { code: 404, message: 'Not found' };

function mockFetch(status: number, body: unknown, ok?: boolean) {
    const resolvedOk = ok ?? (status >= 200 && status < 300);
    return vi.fn().mockResolvedValue({
        ok: resolvedOk,
        status,
        json: () => Promise.resolve(body),
    });
}

// ─────────────────────────────────────────────────────────────────────────────
// Тесты guard-функций (type guards)
// ─────────────────────────────────────────────────────────────────────────────
describe('isUserContactResponse', () => {
    it('возвращает true для корректного объекта', () => {
        expect(isUserContactResponse(validContact)).toBe(true);
    });

    it('возвращает false для null', () => {
        expect(isUserContactResponse(null)).toBe(false);
    });

    it('возвращает false если поле content отсутствует', () => {
        expect(isUserContactResponse({ contactType: 'telegram' })).toBe(false);
    });

    it('возвращает false если contactType не строка', () => {
        expect(isUserContactResponse({ contactType: 42, content: '@alice' })).toBe(false);
    });
});

describe('isUserResponse', () => {
    it('возвращает true для корректного объекта', () => {
        expect(isUserResponse(validUser)).toBe(true);
    });

    it('возвращает false если id не число', () => {
        expect(isUserResponse({ ...validUser, id: '1' })).toBe(false);
    });

    it('возвращает false если contacts не массив', () => {
        expect(isUserResponse({ ...validUser, contacts: {} })).toBe(false);
    });

    it('возвращает false если contacts содержит невалидный элемент', () => {
        expect(isUserResponse({ ...validUser, contacts: [{ wrong: true }] })).toBe(false);
    });
});

describe('isUserResponseArray', () => {
    it('возвращает true для массива валидных пользователей', () => {
        expect(isUserResponseArray(validUserArray)).toBe(true);
    });

    it('возвращает true для пустого массива', () => {
        expect(isUserResponseArray([])).toBe(true);
    });

    it('возвращает false для не-массива', () => {
        expect(isUserResponseArray(validUser)).toBe(false);
    });
});

describe('isResponseMessage', () => {
    it('возвращает true для корректного объекта', () => {
        expect(isResponseMessage(errorMessage)).toBe(true);
    });

    it('возвращает false если code не число', () => {
        expect(isResponseMessage({ code: 'X', message: 'err' })).toBe(false);
    });

    it('возвращает false для null', () => {
        expect(isResponseMessage(null)).toBe(false);
    });
});

// ─────────────────────────────────────────────────────────────────────────────
// Тесты API-функций с мокированием fetch
// ─────────────────────────────────────────────────────────────────────────────
describe('fetchUsers', () => {
    beforeEach(() => { vi.stubGlobal('fetch', mockFetch(200, validUserArray)); });
    afterEach(() => { vi.unstubAllGlobals(); });

    it('возвращает массив пользователей при успешном ответе', async () => {
        const result = await fetchUsers();
        expect(Array.isArray(result)).toBe(true);
        expect((result as typeof validUserArray)[0].username).toBe('alice');
    });

    it('возвращает ResponseMessage при невалидном формате ответа', async () => {
        vi.stubGlobal('fetch', mockFetch(200, { wrong: true }));
        const result = await fetchUsers();
        expect((result as { code: number }).code).toBe(500);
    });

    it('возвращает ResponseMessage при ошибке сети', async () => {
        vi.stubGlobal('fetch', vi.fn().mockRejectedValue(new Error('Network error')));
        const result = await fetchUsers();
        expect((result as { code: number }).code).toBe(0);
        expect((result as { message: string }).message).toBe('Network error');
    });

    it('возвращает ResponseMessage от сервера при HTTP-ошибке', async () => {
        vi.stubGlobal('fetch', mockFetch(404, errorMessage));
        const result = await fetchUsers();
        expect((result as typeof errorMessage).code).toBe(404);
    });
});

describe('fetchUserById', () => {
    afterEach(() => { vi.unstubAllGlobals(); });

    it('возвращает пользователя при успешном ответе', async () => {
        vi.stubGlobal('fetch', mockFetch(200, validUser));
        const result = await fetchUserById(1);
        expect((result as typeof validUser).username).toBe('alice');
    });

    it('возвращает ResponseMessage при HTTP 404', async () => {
        vi.stubGlobal('fetch', mockFetch(404, errorMessage));
        const result = await fetchUserById(99);
        expect((result as { code: number }).code).toBe(404);
    });

    it('возвращает fallback ResponseMessage при невалидном теле ошибки', async () => {
        vi.stubGlobal('fetch', mockFetch(500, null));
        const result = await fetchUserById(1);
        expect((result as { code: number }).code).toBe(500);
    });
});

describe('createUser', () => {
    const payload: UserRequest = { username: 'alice', contacts: [{ contactType: 'telegram', content: '@alice' }] };
    afterEach(() => { vi.unstubAllGlobals(); });

    it('возвращает созданного пользователя', async () => {
        vi.stubGlobal('fetch', mockFetch(200, validUser));
        const result = await createUser(payload);
        expect((result as typeof validUser).id).toBe(1);
    });

    it('передаёт корректный метод и заголовок Content-Type', async () => {
        const spy = mockFetch(200, validUser);
        vi.stubGlobal('fetch', spy);
        await createUser(payload);
        const [, opts] = spy.mock.calls[0] as [string, RequestInit];
        expect(opts.method).toBe('POST');
        expect((opts.headers as Record<string, string>)['Content-Type']).toBe('application/json');
    });

    it('возвращает ResponseMessage при конфликте 409', async () => {
        vi.stubGlobal('fetch', mockFetch(409, { code: 409, message: 'Conflict' }));
        const result = await createUser(payload);
        expect((result as { code: number }).code).toBe(409);
    });
});

describe('deleteUser', () => {
    afterEach(() => { vi.unstubAllGlobals(); });

    it('возвращает { success: true } при статусе 204', async () => {
        vi.stubGlobal('fetch', vi.fn().mockResolvedValue({ status: 204, ok: true }));
        const result = await deleteUser(1);
        expect(result).toEqual({ success: true });
    });

    it('возвращает ResponseMessage при HTTP-ошибке', async () => {
        vi.stubGlobal('fetch', mockFetch(404, errorMessage));
        const result = await deleteUser(99);
        expect((result as { code: number }).code).toBe(404);
    });

    it('возвращает fallback при нечитаемом теле ошибки', async () => {
        vi.stubGlobal('fetch', mockFetch(500, null));
        const result = await deleteUser(1);
        expect((result as { code: number }).code).toBe(500);
    });
});

describe('editUser', () => {
    const payload: UserRequest = { username: 'bob', contacts: [] };
    afterEach(() => { vi.unstubAllGlobals(); });

    it('возвращает обновлённого пользователя', async () => {
        const updated = { ...validUser, username: 'bob' };
        vi.stubGlobal('fetch', mockFetch(200, updated));
        const result = await editUser(1, payload);
        expect((result as typeof validUser).username).toBe('bob');
    });

    it('передаёт метод PUT с телом запроса', async () => {
        const spy = mockFetch(200, { ...validUser, username: 'bob' });
        vi.stubGlobal('fetch', spy);
        await editUser(1, payload);
        const [, opts] = spy.mock.calls[0] as [string, RequestInit];
        expect(opts.method).toBe('PUT');
        expect(opts.body).toBe(JSON.stringify(payload));
    });

    it('возвращает ResponseMessage при ошибке сети', async () => {
        vi.stubGlobal('fetch', vi.fn().mockRejectedValue(new Error('Offline')));
        const result = await editUser(1, payload);
        expect((result as { message: string }).message).toBe('Offline');
    });
});
