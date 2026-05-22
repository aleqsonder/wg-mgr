import { render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { vi } from "vitest";
import type { Mock } from "vitest";

import { UserPage } from "./UserPage";
import {editUser, fetchUserById} from "../api/usersApi";
import { useNavigate, useParams } from "react-router-dom";

vi.mock("../api/usersApi");
vi.mock("react-router-dom", () => ({
    ...vi.importActual("react-router-dom"),
    useNavigate: vi.fn(),
    useParams: vi.fn(),
}));

describe("UserPage", () => {
    const mockNavigate = vi.fn();

    beforeEach(() => {
        (useNavigate as unknown as Mock).mockReturnValue(mockNavigate);
        (useParams as unknown as Mock).mockReturnValue({ id: "1" });

        mockNavigate.mockClear();
        vi.clearAllMocks();
    });

    it("показывает индикатор загрузки", () => {
        (fetchUserById as Mock).mockResolvedValue([]);

        render(<UserPage />);

        expect(screen.getByText("Loading user...")).toBeInTheDocument();
    });

    it("показывает ошибку, если API вернул ошибку", async () => {
        (fetchUserById as Mock).mockResolvedValue({
            code: 404,
            message: "User not found",
        });

        render(<UserPage />);

        expect(await screen.findByText("Error 404")).toBeInTheDocument();
        expect(screen.getByText("User not found")).toBeInTheDocument();
    });

    it("показывает данные пользователя при успешном ответе", async () => {
        (fetchUserById as Mock).mockResolvedValue({
            id: 1,
            username: "Alice",
            contacts: [
                { contactType: "email", content: "alice@example.com" },
                { contactType: "phone", content: "+123456" },
            ],
        });

        render(<UserPage />);

        expect(await screen.findByText("User #1")).toBeInTheDocument();
        expect(screen.getByText("Alice")).toBeInTheDocument();
        expect(screen.getByText("Total contacts: 2")).toBeInTheDocument();

        expect(screen.getByText("email")).toBeInTheDocument();
        expect(screen.getByText("alice@example.com")).toBeInTheDocument();
        expect(screen.getByText("phone")).toBeInTheDocument();
        expect(screen.getByText("+123456")).toBeInTheDocument();
    });

    it("вызывает navigate('/') при клике на кнопку закрытия", async () => {
        (fetchUserById as Mock).mockResolvedValue({
            id: 1,
            username: "Alice",
            contacts: [],
        });

        render(<UserPage />);

        const closeButton = await screen.findByText("✕");
        await userEvent.click(closeButton);

        expect(mockNavigate).toHaveBeenCalledWith("/");
    });

    it("не вызывает setState после размонтирования (isMounted защита)", async () => {
        const resolveLater = () =>
            new Promise((res) =>
                setTimeout(() => res({ id: 1, username: "A", contacts: [] }), 50)
            );

        (fetchUserById as Mock).mockReturnValue(resolveLater());

        const { unmount } = render(<UserPage />);

        unmount();

        await waitFor(() => {
            expect(fetchUserById).toHaveBeenCalled();
        });
    });

    it("переходит в режим редактирования и показывает input", async () => {
        (fetchUserById as Mock).mockResolvedValue({
            id: 1,
            username: "Alice",
            contacts: [],
        });

        render(<UserPage />);

        const editButton = await screen.findByText("Edit");
        await userEvent.click(editButton);

        expect(screen.getByDisplayValue("Alice")).toBeInTheDocument();
    });

    it("меняет имя пользователя в input", async () => {
        (fetchUserById as Mock).mockResolvedValue({
            id: 1,
            username: "Alice",
            contacts: [],
        });

        render(<UserPage />);

        const editButton = await screen.findByText("Edit");
        await userEvent.click(editButton);

        const input = screen.getByDisplayValue("Alice");
        await userEvent.clear(input);
        await userEvent.type(input, "Bob");

        expect(input).toHaveValue("Bob");
    });

    it("сохраняет изменения и выходит из режима редактирования", async () => {
        (fetchUserById as Mock).mockResolvedValue({
            id: 1,
            username: "Alice",
            contacts: [],
        });

        const mockEdit = vi.fn().mockResolvedValue({
            id: 1,
            username: "Bob",
            contacts: [],
        });
        (editUser as Mock).mockImplementation(mockEdit);

        render(<UserPage />);

        const editButton = await screen.findByText("Edit");
        await userEvent.click(editButton);

        const input = screen.getByDisplayValue("Alice");
        await userEvent.clear(input);
        await userEvent.type(input, "Bob");

        const saveButton = screen.getByText("Save");
        await userEvent.click(saveButton);

        expect(mockEdit).toHaveBeenCalledWith(1, {
            username: "Bob",
            contacts: [],
        });

        expect(await screen.findByText("Bob")).toBeInTheDocument();
    });

    it("отменяет редактирование", async () => {
        (fetchUserById as Mock).mockResolvedValue({
            id: 1,
            username: "Alice",
            contacts: [],
        });

        render(<UserPage />);

        const editButton = await screen.findByText("Edit");
        await userEvent.click(editButton);

        const cancelButton = screen.getByText("Cancel");
        await userEvent.click(cancelButton);

        expect(screen.getByText("Alice")).toBeInTheDocument();
    });

});
