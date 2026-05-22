import { render, screen, fireEvent } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import { UserItem } from './UserItem';

const baseUser = {
    id: 1,
    username: 'alice',
    contacts: [
        { contactType: 'telegram', content: '@alice' },
        { contactType: 'email', content: 'alice@example.com' },
    ],
};

describe('UserItem', () => {
    it('отображает имя пользователя', () => {
        render(<UserItem user={baseUser} onDelete={vi.fn()} onEdit={vi.fn()} />);
        expect(screen.getByText('alice')).toBeInTheDocument();
    });

    it('отображает все контакты пользователя', () => {
        render(<UserItem user={baseUser} onDelete={vi.fn()} onEdit={vi.fn()} />);
        expect(screen.getByText('@alice')).toBeInTheDocument();
        expect(screen.getByText('alice@example.com')).toBeInTheDocument();
    });

    it('вызывает onDelete с корректным id при нажатии кнопки удаления', () => {
        const onDelete = vi.fn();
        render(<UserItem user={baseUser} onDelete={onDelete} onEdit={vi.fn()} />);
        fireEvent.click(screen.getByRole('button', { name: /delete|удалить/i }));
        expect(onDelete).toHaveBeenCalledWith(1);
        expect(onDelete).toHaveBeenCalledTimes(1);
    });

    it('вызывает onEdit с объектом пользователя при нажатии кнопки редактирования', () => {
        const onEdit = vi.fn();
        render(<UserItem user={baseUser} onDelete={vi.fn()} onEdit={onEdit} />);
        fireEvent.click(screen.getByRole('button', { name: /edit|редактировать/i }));
        expect(onEdit).toHaveBeenCalledWith(baseUser);
        expect(onEdit).toHaveBeenCalledTimes(1);
    });

    it('отображает компонент без контактов если список пустой', () => {
        const userNoContacts = { ...baseUser, contacts: [] };
        render(<UserItem user={userNoContacts} onDelete={vi.fn()} onEdit={vi.fn()} />);
        expect(screen.getByText('alice')).toBeInTheDocument();
    });

    it('корректно рендерится для разных пользователей (snapshot-стиль)', () => {
        const { container } = render(
            <UserItem user={{ id: 2, username: 'bob', contacts: [] }} onDelete={vi.fn()} onEdit={vi.fn()} />
        );
        expect(container.firstChild).toBeTruthy();
        expect(screen.getByText('bob')).toBeInTheDocument();
    });
});
